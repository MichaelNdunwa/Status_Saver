package com.michael.statussaver.utils

import android.net.Uri
import android.os.Build

object Constants {
    const val STATUS_TYPE_WHATSAPP = "com.whatsapp"
    const val MEDIA_TYPE_WHATSAPP_IMAGES = "com.whatsapp.images"
    const val MEDIA_TYPE_WHATSAPP_VIDEOS = "com.whatsapp.videos"
    const val MEDIA_TYPE_WHATSAPP_AUDIOS = "com.whatsapp.audios"

    const val STATUS_TYPE_WHATSAPP_BUSINESS = "com.whatsapp.w4b"
    const val MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES = "com.whatsapp.w4b.images"
    const val MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS = "com.whatsapp.w4b.videos"
    const val MEDIA_TYPE_WHATSAPP_BUSINESS_AUDIOS = "com.whatsapp.w4b.audios"

    // Downloaded Status:
    const val STATUS_TYPE_DOWNLOADED = "downloaded"
    const val MEDIA_TYPE_DOWNLOADED_IMAGES = "downloaded_images"
    const val MEDIA_TYPE_DOWNLOADED_VIDEOS = "downloaded_videos"
    const val MEDIA_TYPE_DOWNLOADED_AUDIOS = "downloaded_audios"

    const val MEDIA_LIST_KEY = "MEDIA_LIST"
    const val MEDIA_SCROLL_KEY = "MEDIA_SCROLL"
    const val MEDIA_TYPE_KEY = "MEDIA_TYPE"

    const val FRAGMENT_TYPE_KEY = "FRAGMENT_TYPE"

    // WhatsApp Status Path URI:
    val WHATSAPP_PATH_URI_ANDROID =
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AWhatsApp%2AWhatsApp%2FMedia%2F.Statuses")
    val WHATSAPP_PATH_URI_ANDROID_11 =
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses")

    val WHATSAPP_BUSINESS_PATH_URI_ANDROID =
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AWhatsApp%20Business%2FMedia%2F.Statuses")
    val WHATSAPP_BUSINESS_PATH_URI_ANDROID_11 =
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses")

//    val DOWNLOADED_PATH_URI_ANDROID =
//        Uri.parse("content://com.android.externalstorage.documents/document/primary%3APictures%2FStatus%20Saver")
//    val DOWNLOADED_PATH_URI_ANDROID_11 =
//        Uri.parse("content://com.android.externalstorage.documents/document/primary%3APictures%2FStatus%20Saver")

//    val DOWNLOADED_PATH_URI_ANDROID =
//        Uri.parse("content://com.android.externalstorage.documents/document/primary%3A")
//    val DOWNLOADED_PATH_URI_ANDROID_11 =
//        Uri.parse("content://com.android.externalstorage.documents/document/primary%3A")

    val DOWNLOADED_PATH_URI_ANDROID = listOf(
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3APictures%2FStatus%20Saver"), // For images and videos
        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AMusic%2FStatus%20Saver")     // For audio
    )

    fun getWhatsAppStatusUriPath(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WHATSAPP_PATH_URI_ANDROID_11
        } else {
            WHATSAPP_PATH_URI_ANDROID
        }
    }
    fun getWhatsAppBusinessStatusUriPath(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WHATSAPP_BUSINESS_PATH_URI_ANDROID_11
        } else {
            WHATSAPP_BUSINESS_PATH_URI_ANDROID
        }
    }
//    fun getDownloadedStatusUriPath(): Uri {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            DOWNLOADED_PATH_URI_ANDROID_11
//        } else {
//            DOWNLOADED_PATH_URI_ANDROID
//        }
//    }

    fun getDownloadedStatusUriPath(): List<Uri> = DOWNLOADED_PATH_URI_ANDROID

}