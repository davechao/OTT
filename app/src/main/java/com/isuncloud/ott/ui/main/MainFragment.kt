package com.isuncloud.ott.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.VerticalGridSupportFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isuncloud.ott.R
import com.isuncloud.ott.presenter.CardItemPresenter
import com.isuncloud.ott.repository.model.AppItem
import com.isuncloud.ott.ui.MainViewModel

class MainFragment: VerticalGridSupportFragment() {

    companion object {
        private const val NUM_COLUMNS = 5
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupListener()
        setupData()
        createECKey()
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.isClickApp) {
            viewModel.exitApp()
        }
        viewModel.isClickApp = false
    }

    private fun setupView() {
        val gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = MainFragment.NUM_COLUMNS
        setGridPresenter(gridPresenter)
        title = getString(R.string.main_name)
        searchAffordanceColor = ContextCompat.getColor(context!!, R.color.search_opaque)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private fun setupListener() {
        onItemViewClickedListener = ItemViewClickedListener()
        setOnItemViewSelectedListener(ItemViewSelectedListener())
    }

    private fun setupData() {
        var cardRowAdapter = ArrayObjectAdapter(CardItemPresenter())

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val packageManager = activity!!.packageManager
        val apps = packageManager.queryIntentActivities(intent, 0)
        apps.forEach {
            val appInfo = AppItem(
                    it.activityInfo.packageName,
                    it.loadLabel(packageManager).toString(),
                    it.activityInfo.loadIcon(packageManager))
            cardRowAdapter.add(appInfo)
        }

        adapter = cardRowAdapter
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

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
                itemViewHolder: Presenter.ViewHolder?,
                item: Any?,
                rowViewHolder: RowPresenter.ViewHolder?,
                row: Row?) {

        }
    }

}