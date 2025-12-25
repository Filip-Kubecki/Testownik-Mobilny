package com.example.testownik_mobilny.logic

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.File

object TestInfoManager {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private const val FILE_NAME = "test_info.json"

    fun saveTestInfo(databaseDir: File, info: TestInfo) {
        try {
            val parentDir = databaseDir.parentFile
            if (parentDir != null) {
                val file = File(parentDir, "test_info.json")

                // LOG THIS: It will show you the exact path in Logcat
                android.util.Log.d("STORAGE_DEBUG", "Saving to: ${file.absolutePath}")

                val jsonString = json.encodeToString(info)
                file.writeText(jsonString)

                android.util.Log.d("STORAGE_DEBUG", "Save Successful! File size: ${file.length()} bytes")
            } else {
                android.util.Log.e("STORAGE_DEBUG", "Parent directory was null!")
            }
        } catch (e: Exception) {
            android.util.Log.e("STORAGE_DEBUG", "Failed to save: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadTestInfo(databaseDir: File): TestInfo? {
        val parentDir = databaseDir.parentFile
        val file = File(parentDir, FILE_NAME)

        return if (file.exists()) {
            try {
                val jsonString = file.readText()
                json.decodeFromString<TestInfo>(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }
}