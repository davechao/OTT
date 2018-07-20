package com.isuncloud.ott.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v17.leanback.app.VerticalGridFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.isuncloud.ott.R
import com.isuncloud.ott.presenter.CardItemPresenter
import com.isuncloud.ott.repository.model.app.AppItem
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainFragment: VerticalGridFragment() {

    companion object {
        private const val NUM_COLUMNS = 5
        private const val COLLECTION_PATH = "OTT"
    }

    lateinit var db: FirebaseFirestore

    private var isClickApp = false

    private lateinit var startDate: Date
    private lateinit var documentId: String

    private var sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFirestore()
        setupListener()
        setupData()
    }

    override fun onResume() {
        super.onResume()

        if(isClickApp) {
            val endDate = Date()
            val duration = (endDate.time - startDate.time) / 1000
            val appItemMap = hashMapOf<String, Any>()
            appItemMap["APPETime"] = sdf.format(endDate)
            appItemMap["APPrunduration"] = duration

            db.collection(COLLECTION_PATH)
                    .document(documentId)
                    .set(appItemMap, SetOptions.merge())
                    .addOnSuccessListener {
                        Timber.d("data written successfully!")
                    }
                    .addOnFailureListener {
                        Timber.d("data written fail!")
                    }
        }

        isClickApp = false
    }

    private fun setupView() {
        val gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = MainFragment.NUM_COLUMNS
        setGridPresenter(gridPresenter)
        title = getString(R.string.main_name)
        searchAffordanceColor = ContextCompat.getColor(activity, R.color.search_opaque)
    }

    private fun setupFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun setupListener() {
        onItemViewClickedListener = ItemViewClickedListener()
        setOnItemViewSelectedListener(ItemViewSelectedListener())
    }

    private fun setupData() {
        var cardRowAdapter = ArrayObjectAdapter(CardItemPresenter())

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)

        val packageManager = activity.packageManager
        val apps = packageManager.queryIntentActivities(intent, 0)
        apps.forEach {
            if(it.activityInfo.banner != 0) {
                val appInfo = AppItem(
                        it.activityInfo.packageName,
                        it.loadLabel(packageManager).toString(),
                        it.activityInfo.loadBanner(packageManager))
                cardRowAdapter.add(appInfo)
            } else {
                val appInfo = AppItem(
                        it.activityInfo.packageName,
                        it.loadLabel(packageManager).toString(),
                        it.activityInfo.loadIcon(packageManager))
                cardRowAdapter.add(appInfo)
            }
        }

        adapter = cardRowAdapter
    }

    private inner class ItemViewClickedListener: OnItemViewClickedListener {
        override fun onItemClicked(
                itemViewHolder: Presenter.ViewHolder?,
                item: Any?,
                rowViewHolder: RowPresenter.ViewHolder?,
                row: Row?) {
            if(item is AppItem) {
                startDate = Date()

                val appItemMap = hashMapOf<String, Any>()
                appItemMap["DeviceId"] = Build.SERIAL
                appItemMap["APPId"] = item.appId
                appItemMap["APPName"] = item.appName
                appItemMap["APPSTime"] = sdf.format(startDate)

                db.collection(COLLECTION_PATH)
                        .add(appItemMap)
                        .addOnSuccessListener {
                            documentId = it.id
                            Timber.d("data written with ID: " + it.id)
                        }
                        .addOnFailureListener {
                            Timber.d("data written fail!")
                        }

                isClickApp = true

                val intent = activity.packageManager
                        .getLeanbackLaunchIntentForPackage(item.appId)
                activity.startActivity(intent)
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