package com.michael.statussaver.models

import java.io.Serializable

const val MEDIA_TYPE_IMAGE = "image"
const val MEDIA_TYPE_VIDEO = "video"
const val MEDIA_TYPE_AUDIO = "audio"

data class MediaModel(
    val pathUri: String,
    val fileName: String,
    val fileType: String = MEDIA_TYPE_IMAGE,
    var isDownloaded: Boolean = false
): Serializable
