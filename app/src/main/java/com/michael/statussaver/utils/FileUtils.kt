package com.michael.statussaver.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.toRawFile
import java.io.File
import com.michael.statussaver.R
import com.michael.statussaver.models.MediaModel
import java.io.FileInputStream
import java.io.FileOutputStream

fun Context.isStatusExist(fileName: String): Boolean {
    val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val file = File("${downloadDirectory}/${getString(R.string.app_name)}", fileName)
    return file.exists()
}

fun getFileExtension(fileName: String): String {
    val lastDotIndex = fileName.lastIndexOf(".")
    if (lastDotIndex >= 0 && lastDotIndex < fileName.length - 1) {
        return fileName.substring(lastDotIndex + 1)
    }
    return ""
}

fun Context.saveStatus(model: MediaModel): Boolean {
    if (isStatusExist(model.fileName)) {
        return true
    }
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        return saveStatusBeforeQ(this, Uri.parse(model.pathUri))
    }
    val extension = getFileExtension(model.fileName)
    val mimeType = "${model.fileType}/$extension"
    val inputStream = contentResolver.openInputStream(model.pathUri.toUri())
    try {
        val values = ContentValues()
        values.apply {
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.DISPLAY_NAME, model.fileName)
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOCUMENTS + "/" + getString(R.string.app_name)
            )
        }
        val uri = contentResolver.insert(
            MediaStore.Files.getContentUri("external"),
            values
        )
        uri?.let {
            val outputStream = contentResolver.openOutputStream(it)
            if (inputStream != null) {
                outputStream?.write(inputStream.readBytes())
            }
            outputStream?.close()
            inputStream?.close()
            return true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

private fun saveStatusBeforeQ(context: Context, uri: Uri): Boolean {
    try {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        if (documentFile != null) {
            val sourceFile = documentFile.toRawFile(context)?.takeIf { f2 ->
//                f2.canRead() && f2.isFile && f2.canWrite()
                f2.canRead()
            }
            val destinationFile = sourceFile?.let { sourceFile ->
                File(
                    "${Environment.getExternalStorageDirectory()}/Document/${context.getString(R.string.app_name)}",
                    sourceFile.name
                )
            }
            destinationFile?.let { destFile ->
                // making sure the parent directory exists and creating it if it doesn't
                if (!destFile.parentFile?.exists()!!) {
                    destFile.mkdirs()
                }
                if (!destFile.exists()) {
                    destFile.createNewFile()
                }
                // copying content from dest file to source file:
                val source = FileInputStream(sourceFile).channel
                val destination = FileOutputStream(destFile).channel
                destination.transferFrom(source, 0, source.size())
                source.close()
                destination.close()
                return true
            }
        }
        return false
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}