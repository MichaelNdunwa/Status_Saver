package com.michael.statussaver.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.toRawFile
import java.io.File
import com.michael.statussaver.R
import com.michael.statussaver.models.MediaModel
import java.io.FileInputStream
import java.io.FileOutputStream

//fun Context.isStatusExist(fileName: String): Boolean {
//    // To save the status in the Pictures directory:
//    val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//    val file = File("${downloadDirectory}/${getString(R.string.app_name)}", fileName)
//    return file.exists()
//}

fun Context.isStatusExist(fileName: String, fileType: String): Boolean {
    // Define the directories where statuses are stored
    val downloadDirectory = when (fileType) {
        "image", "video" -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        "audio" -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        else -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }
    // Check each directory for the saved status file
    val file = File("${downloadDirectory}/${getString(R.string.app_name)}", fileName)
    return file.exists()
}

fun getFileExtension(fileName: String): String {
    val lastDotIndex = fileName.lastIndexOf(".")
    if (lastDotIndex >= 0 && lastDotIndex < fileName.length - 1) {
        return fileName.substring(lastDotIndex + 1)
//        return fileName.substring(lastDotIndex - 1)
    }
    return ""
}

fun Context.saveStatus(model: MediaModel): Boolean {
    if (isStatusExist(model.fileName, model.fileType)) {
        return true
    }
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        return saveStatusBeforeQ(this, Uri.parse(model.pathUri), model.fileType)
    }
    val extension = getFileExtension(model.fileName)
    val mimeType = "${model.fileType}/$extension"
    val inputStream = contentResolver.openInputStream(model.pathUri.toUri())
    try {
        val values = ContentValues()
        values.apply {
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.DISPLAY_NAME, model.fileName)
//            put(
//                MediaStore.MediaColumns.RELATIVE_PATH,
////                Environment.DIRECTORY_DOCUMENTS + "/" + getString(R.string.app_name)
//                  Environment.DIRECTORY_PICTURES + "/" + getString(R.string.app_name)
//            )
            // Setting the relative path base on file type:
            val relativePath = when (model.fileType) {
                "image", "video" -> Environment.DIRECTORY_PICTURES + "/" + getString(R.string.app_name)
//                "video" -> Environment.DIRECTORY_PICTURES + "/" + getString(R.string.app_name)
                "audio" -> Environment.DIRECTORY_MUSIC + "/" + getString(R.string.app_name)
                else -> Environment.DIRECTORY_DOWNLOADS + "/" + getString(R.string.app_name)
            }
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }
        //val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
        val uri = when (model.fileType) {
            "image" -> contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            "video" -> contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
            "audio" -> contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
            else -> contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        }
        uri?.let {
            val outputStream = contentResolver.openOutputStream(it)
            if (inputStream != null) {
                outputStream?.write(inputStream.readBytes())
            }
            outputStream?.close()
            inputStream?.close()
            return true
        }  ?: run {
            Log.d("SaveToPictures", "${model.fileType} : Failed to insert URI")
            Log.d("SaveToPictures", "${model.fileName} : Failed to insert URI")
            return false
        }
    } catch (e: Exception) {
        e.printStackTrace()
         Log.d("SaveToPictures", "${model.fileType} : ${model.fileName} : Error saving status", e)
        return false
    }
    return false
}

private fun saveStatusBeforeQ(context: Context, uri: Uri, fileType: String): Boolean {
    try {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        if (documentFile != null) {
            val sourceFile = documentFile.toRawFile(context)?.takeIf { f2 ->
                //f2.canRead() && f2.isFile && f2.canWrite()
                f2.canRead()
            }
            val destinationFile = sourceFile?.let { media ->
//                File("${Environment.getExternalStorageDirectory()}/Pictures/${context.getString(R.string.app_name)}", media.name)
                // Determine the destination directory based on the file type
                val directoryPath = when (fileType) {
                    "image", "video" -> "${Environment.getExternalStorageDirectory()}/Pictures/${context.getString(R.string.app_name)}"
                    "audio" -> "${Environment.getExternalStorageDirectory()}/Music/${context.getString(R.string.app_name)}"
                    else -> "${Environment.getExternalStorageDirectory()}/Download/${context.getString(R.string.app_name)}"
                }
                File(directoryPath, media.name)
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