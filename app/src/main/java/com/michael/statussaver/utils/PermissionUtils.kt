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
    activity.startActivityForResult(intent, REQUET_CODE)
}