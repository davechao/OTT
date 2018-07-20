package com.isuncloud.ott.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class ApiRepository {

    companion object {
        private const val COLLECTION_PATH = "OTT"
        val dateTimeFormatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        fun dateTimeFormat(date: Date) {
            dateTimeFormatter.format(date)
        }
    }

    @Inject
    lateinit var firestore: FirebaseFirestore

    fun addAppItem(appItemMap: HashMap<String, Any>)
            = firestore.collection(COLLECTION_PATH).add(appItemMap)

    fun updateAppItem(appItemMap: HashMap<String, Any>, documentId: String)
            = firestore.collection(COLLECTION_PATH)
                .document(documentId)
                .set(appItemMap, SetOptions.merge())

}