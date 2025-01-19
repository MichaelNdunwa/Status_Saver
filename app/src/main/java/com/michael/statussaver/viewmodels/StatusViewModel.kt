package com.michael.statussaver.viewmodels

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michael.statussaver.data.StatusRepository
import com.michael.statussaver.models.MEDIA_TYPE_AUDIO
import com.michael.statussaver.models.MEDIA_TYPE_IMAGE
import com.michael.statussaver.models.MEDIA_TYPE_VIDEO
import com.michael.statussaver.models.MediaModel
import com.michael.statussaver.utils.Constants
import com.michael.statussaver.utils.SharedPrefKeys
import com.michael.statussaver.utils.SharedPrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusViewModel(val repository: StatusRepository) : ViewModel() {
    private val wpStatusLiveData get() = repository.whatsAppStatusLiveData
    private val wpBusinessStatusLiveData get() = repository.whatsAppBusinessStatusLiveData
    private val downloadStatusLiveData get() = repository.downloadStatusLiveData
    private val TAG = "StatusViewModel"

    // whatsAppLiveData
    val whatsAppImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppAudiosLiveData = MutableLiveData<ArrayList<MediaModel>>()

    // whatsAppBusinessLiveData
    val whatsAppBusinessImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessAudiosLiveData = MutableLiveData<ArrayList<MediaModel>>()

    // downloadLiveData
    val downloadImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val downloadVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val downloadAudiosLiveData = MutableLiveData<ArrayList<MediaModel>>()

    private var isPermissionsGranted = false
    private var isWhatsAppPermissionGranted = false
    private var isWhatsAppBusinessPermissionGranted = false
    private var isDownloadPermissionGranted = false

    init {
        SharedPrefUtils.init(repository.activity)
        val whatsAppPermission =
            SharedPrefUtils.getPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED, false)
        val whatsAppBusinessPermission =
            SharedPrefUtils.getPrefBoolean(
                SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED,
                false
            )
        val downloadPermission =
            SharedPrefUtils.getPrefBoolean(
                SharedPrefKeys.PREF_KEY_DOWNLOADED_PERMISSION_GRANTED,
                false
            )
        isWhatsAppPermissionGranted = whatsAppPermission
        isWhatsAppBusinessPermissionGranted = whatsAppBusinessPermission
        isDownloadPermissionGranted = downloadPermission

//        isPermissionsGranted = whatsAppPermission && whatsAppBusinessPermission

        when {
            isWhatsAppPermissionGranted -> {
                Log.d(TAG, "init: whatsAppPermission is true")
                CoroutineScope(Dispatchers.IO).launch {
                    repository.getAllStatus(Constants.STATUS_TYPE_WHATSAPP)
                }
            }

            isWhatsAppBusinessPermissionGranted -> {
                Log.d(TAG, "init: whatsAppBusinessPermission is true")
                CoroutineScope(Dispatchers.IO).launch {
                    repository.getAllStatus(Constants.STATUS_TYPE_WHATSAPP_BUSINESS)
                }
            }

            isDownloadPermissionGranted -> {
                Log.d(TAG, "init: downloadPermission is true")
                CoroutineScope(Dispatchers.IO).launch {
                    repository.getAllStatus(Constants.STATUS_TYPE_DOWNLOADED)
                }
            }
        }
    }

    fun getWhatsAppStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isWhatsAppPermissionGranted) {
                Log.d(TAG, "getWhatsAppStatus: Requesting Whatsapp Status")
                repository.getAllStatus(Constants.STATUS_TYPE_WHATSAPP)
            }
            withContext(Dispatchers.Main) {
                getWhatsAppStatusImages()
                getWhatsAppStatusVideos()
                getWhatsAppStatusAudios()
            }
        }
    }

    fun getWhatsAppStatusImages() {
        wpStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_IMAGE) tempList.add(mediaModel)
            }
            whatsAppImagesLiveData.postValue(tempList)
        }
    }
    fun getWhatsAppStatusVideos() {
        wpStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_VIDEO) tempList.add(mediaModel)
            }
            whatsAppVideosLiveData.postValue(tempList)
        }
    }
    fun getWhatsAppStatusAudios() {
        wpStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_AUDIO) tempList.add(mediaModel)
            }
            whatsAppAudiosLiveData.postValue(tempList)
        }
    }


    fun getWhatsAppBusinessStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isWhatsAppBusinessPermissionGranted) {
                Log.d(TAG, "getWhatsAppBusinessStatus: Requesting Whatsapp Status")
                repository.getAllStatus(Constants.STATUS_TYPE_WHATSAPP_BUSINESS)
            }
            withContext(Dispatchers.Main) {
                getWhatsAppBusinessStatusImages()
                getWhatsAppBusinessStatusVideos()
                getWhatsAppBusinessStatusAudios()
            }
        }
    }
    fun getWhatsAppBusinessStatusImages() {
        wpBusinessStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_IMAGE) tempList.add(mediaModel)
            }
            whatsAppBusinessImagesLiveData.postValue(tempList)
        }
    }
    fun getWhatsAppBusinessStatusVideos() {
        wpBusinessStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_VIDEO) tempList.add(mediaModel)
            }
            whatsAppBusinessVideosLiveData.postValue(tempList)
        }
    }
    fun getWhatsAppBusinessStatusAudios() {
        wpBusinessStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_AUDIO) tempList.add(mediaModel)
            }
            whatsAppBusinessAudiosLiveData.postValue(tempList)
        }
    }

    fun getDownloadedStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isDownloadPermissionGranted) {
                Log.d(TAG, "getDownloadedStatus: Requesting Download Status")
                repository.getAllStatus(Constants.STATUS_TYPE_DOWNLOADED)
            }
            withContext(Dispatchers.Main) {
                getDownloadedStatusImages()
                getDownloadedStatusVideos()
                getDownloadedStatusAudios()
            }
        }
    }
    fun getDownloadedStatusImages() {
        downloadStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_IMAGE) tempList.add(mediaModel)
            }
            downloadImagesLiveData.postValue(tempList)
        }
    }
    fun getDownloadedStatusVideos() {
        downloadStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_VIDEO) tempList.add(mediaModel)
            }
            downloadVideosLiveData.postValue(tempList)
        }
    }
    fun getDownloadedStatusAudios() {
        downloadStatusLiveData.observe(repository.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.fileType == MEDIA_TYPE_AUDIO) tempList.add(mediaModel)
            }
            downloadAudiosLiveData.postValue(tempList)
        }
    }
}