package com.example.testownik_mobilny

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.testownik_mobilny.ui.theme.databaseDirectoryName
import java.io.BufferedInputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

fun unZip(context: Context, zipUri: Uri, targetDirName: String) {
    val targetDir = File(context.filesDir.resolve(databaseDirectoryName), targetDirName)
    if (!targetDir.exists()) targetDir.mkdirs()

    Log.d("SELF","NAME: $targetDirName")

    context.contentResolver.openInputStream(zipUri)?.use { inputStream ->
        ZipInputStream(BufferedInputStream(inputStream)).use { zipStream ->
            var entry = zipStream.nextEntry
            while (entry != null) {
                val outFile = File(targetDir, entry.name)

                if (entry.isDirectory) {
                    outFile.mkdirs()
                } else {
                    outFile.parentFile?.mkdirs()

                    FileOutputStream(outFile).use { output ->
                        zipStream.copyTo(output)
                    }
                }

                zipStream.closeEntry()
                entry = zipStream.nextEntry
            }
            Log.d("SELF", "UNZIPED")
        }
    } ?: throw FileNotFoundException("Cannot open URI: $zipUri")
}

fun removeImgTag(value : String) : String{
    return value.replace("[img]","").replace("[/img]","")
}

fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    result = it.getString(nameIndex)
                }
            }
        }
    }

    if (result == null) {
        result = uri.path?.substringAfterLast('/')
    }

    return result
}