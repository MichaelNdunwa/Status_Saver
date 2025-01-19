package com.michael.statussaver.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import com.michael.statussaver.models.MEDIA_TYPE_AUDIO
import com.michael.statussaver.models.MEDIA_TYPE_IMAGE
import com.michael.statussaver.models.MEDIA_TYPE_VIDEO
import com.michael.statussaver.models.MediaModel
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.utils.SharedPrefKeys
import com.michael.statussaver.utils.SharedPrefUtils
import com.michael.statussaver.utils.getFileExtension
import com.michael.statussaver.utils.isStatusExist

class StatusRepository(val context: Context) {

    val whatsAppStatusLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessStatusLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val downloadStatusLiveData =  MutableLiveData<ArrayList<MediaModel>>()
    val activity = context as Activity
    private val wpStatusList = ArrayList<MediaModel>()
    private val wpBusinessStatusList = ArrayList<MediaModel>()
    private val downloadStatusList = ArrayList<MediaModel>()
    private val TAG = "StatusRepository"

    fun getAllStatus(type: String) {
        val treeUri = when (type) {
            Constants.STATUS_TYPE_WHATSAPP -> {
                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_WP_TREE_URI, "")?.toUri()!!
            }
            Constants.STATUS_TYPE_WHATSAPP_BUSINESS -> {
                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI, "")?.toUri()!!
            }
            Constants.STATUS_TYPE_DOWNLOADED -> {
                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_DOWNLOADED_TREE_URI, "")?.toUri()!!
            }
            else -> null
        }
//        val treeUri = when (type) {
//            Constants.STATUS_TYPE_WHATSAPP -> {
//                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_WP_TREE_URI, "")?.toUri()!!
//            }
//            else -> {
//                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI, "")?.toUri()!!
//            }
//        }
        Log.d(TAG, "getAllStatus: $treeUri")

        activity.contentResolver.takePersistableUriPermission(
            treeUri!!,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        val fileDocument = DocumentFile.fromTreeUri(activity, treeUri)

        fileDocument?.let {
            it.listFiles().forEach { file ->
                Log.d(TAG, "getAllStatus: ${file.name}")
                if (file.name != ".nomedia" && file.isFile) {
                    val isDownloaded = activity.isStatusExist(file.name.toString(), file.uri.toString())
//                    val fileType = if (getFileExtension(file.name ?: "") == "mp4") {
//                        MEDIA_TYPE_VIDEO
//                    } else {
//                        MEDIA_TYPE_IMAGE
//                    }
                    val fileType = when(getFileExtension(file.name ?: "")) {
                        "mp4" -> MEDIA_TYPE_VIDEO
                        "jpg", "png", "jpeg" -> MEDIA_TYPE_IMAGE
                        "opus" -> MEDIA_TYPE_AUDIO
                        else -> ""

                    }
                    val model = MediaModel(
                        pathUri = file.uri.toString(),
                        fileName = file.name ?: "",
                        fileType = fileType,
                        isDownloaded = isDownloaded
                    )
                    when (type) {
                        Constants.STATUS_TYPE_WHATSAPP -> wpStatusList.add(model)
                        Constants.STATUS_TYPE_WHATSAPP_BUSINESS -> wpBusinessStatusList.add(model)
                        Constants.STATUS_TYPE_DOWNLOADED -> downloadStatusList.add(model)
                    }
                }
            }
            when (type) {
                Constants.STATUS_TYPE_WHATSAPP -> {
                    Log.d(TAG, "getAllStatus: Pushing Value to WhatsApp Status LiveData")
                    whatsAppStatusLiveData.postValue(wpStatusList)
                }
                Constants.STATUS_TYPE_WHATSAPP_BUSINESS -> {
                    Log.d(TAG, "getAllStatus: Pushing Value to WhatsApp Business Status LiveData")
                    whatsAppBusinessStatusLiveData.postValue(wpBusinessStatusList) // I can't forget what happened, I used a whole 24hrs to debug this lol.
                }
                Constants.STATUS_TYPE_DOWNLOADED -> {
                    Log.d(TAG, "getAllStatus: Pushing Value to Download Status LiveData")
                    downloadStatusLiveData.postValue(downloadStatusList)
                }
            }
        }
    }
}