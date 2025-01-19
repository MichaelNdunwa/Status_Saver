package com.michael.statussaver.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract

fun getFolderPermissions(context: Context, REQUET_CODE: Int, initialUri: Uri) {
    val activity = context as Activity
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
    intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
    activity.startActivityForResult(intent, REQUET_CODE)
}


fun getAllDownloadedFolderPermissions(context: Context, REQUET_CODE: Int, initialUri: List<Uri>) {
    val activity = context as Activity

    // List of all required directories
//    val urisToGrant = listOf(
//        Uri.parse("content://com.android.externalstorage.documents/document/primary%3APictures%2FStatus%20Saver"), // For images and videos
//        Uri.parse("content://com.android.externalstorage.documents/document/primary%3AMusic%2FStatus%20Saver")     // For audio
//    )
//    urisToGrant.forEach { initialUri ->
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
//        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
//        activity.startActivityForResult(intent, REQUET_CODE)
//    }

    initialUri.forEach { uriPath ->
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriPath)
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
        activity.startActivityForResult(intent, REQUET_CODE)
    }
}
