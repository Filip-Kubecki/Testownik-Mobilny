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
            val file = File(databaseDir, FILE_NAME)

            android.util.Log.d("STORAGE_DEBUG", "Saving to: ${file.absolutePath}")

            val jsonString = json.encodeToString(info)
            file.writeText(jsonString)

            android.util.Log.d("STORAGE_DEBUG", "Save Successful! File size: ${file.length()} bytes")
        } catch (e: Exception) {
            android.util.Log.e("STORAGE_DEBUG", "Failed to save: ${e.message}")
            e.printStackTrace()
        }
    }

    fun loadTestInfo(databaseDir: File): TestInfo? {
        val file = File(databaseDir, FILE_NAME)

        return if (file.exists()) {
            try {
                val jsonString = file.readText()
                json.decodeFromString<TestInfo>(jsonString)
            } catch (e: Exception) {
                android.util.Log.e("STORAGE_DEBUG", "Failed to load: ${e.message}")
                e.printStackTrace()
                null
            }
        } else {
            android.util.Log.d("STORAGE_DEBUG", "No info file found at: ${file.absolutePath}")
            null
        }
    }
}