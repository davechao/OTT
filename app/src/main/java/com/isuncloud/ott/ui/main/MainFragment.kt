package com.isuncloud.ott.ui.main

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import android.support.v17.leanback.app.VerticalGridSupportFragment
import android.support.v17.leanback.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isuncloud.ott.BuildConfig
import com.isuncloud.ott.R
import com.isuncloud.ott.presenter.CardItemPresenter
import com.isuncloud.ott.repository.model.AppItem

class MainFragment: VerticalGridSupportFragment(), LifecycleObserver {

    companion object {
        private const val NUM_COLUMNS = 5
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupView()
        setupObserver()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupListener()
        setupData()

        // TODO: if it has network connected, then createECKey
        createECKey()
    }

    private fun setupView() {
        val gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = MainFragment.NUM_COLUMNS
        setGridPresenter(gridPresenter)
        title = getString(R.string.main_name)
    }

    private fun setupObserver() {
        lifecycle.addObserver(this)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private fun setupListener() {
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private fun setupData() {
        viewModel.androidId = Secure.getString(activity?.contentResolver, Secure.ANDROID_ID)

        var cardRowAdapter = ArrayObjectAdapter(CardItemPresenter())

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val packageManager = activity!!.packageManager
        val apps = packageManager.queryIntentActivities(intent, 0)
        apps.forEach {
            if(it.activityInfo.packageName != BuildConfig.APPLICATION_ID) {
                val appInfo = AppItem(
                        it.activityInfo.packageName,
                        it.loadLabel(packageManager).toString(),
                        it.activityInfo.loadIcon(packageManager))
                cardRowAdapter.add(appInfo)
            }
        }

        adapter = cardRowAdapter
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun backToLauncher() {
        if(viewModel.isClickApp) {
            viewModel.exitApp()
        }
        viewModel.isClickApp = false
    }

    private fun createECKey() {
        viewModel.createEcKeyPair()
    }

    private inner class ItemViewClickedListener: OnItemViewClickedListener {
        override fun onItemClicked(
                itemViewHolder: Presenter.ViewHolder?,
                item: Any?,
                rowViewHolder: RowPresenter.ViewHolder?,
                row: Row?) {
            if(item is AppItem) {
                viewModel.enterApp(item)
                viewModel.isClickApp = true

                val intent = activity!!.packageManager
                        .getLaunchIntentForPackage(item.appId)
                context!!.startActivity(intent)
            }
        }
    }
}