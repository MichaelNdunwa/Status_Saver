package com.michael.statussaver.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.michael.statussaver.data.StatusRepository
import com.michael.statussaver.models.MediaModel

class StatusViewModel(val repository: StatusRepository) : ViewModel() {
//    private val wpStatusLiveData get() = repository.whatsAppStatusLiveData
//    private val wpBusinessStatusLiveData get() = repository.whatsAppBusinessStatusLiveData
    private val TAG = "StatusViewModel"

    val whatsAppImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()

    private var isPermissionsGranted = false
}