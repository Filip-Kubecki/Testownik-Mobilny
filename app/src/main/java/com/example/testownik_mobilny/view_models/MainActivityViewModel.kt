package com.example.testownik_mobilny.view_models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.logic.TestFilesParser
import com.example.testownik_mobilny.ui.theme.databaseDirectoryName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivityViewModel: ViewModel() {
    var settingsEnabled = mutableStateOf(false)
        private set

    var databaseList = mutableStateListOf<QuestionDatabase>()
        private set

    fun toggleSettings(){
        settingsEnabled.value = true
    }

    fun disableSettings(){
        settingsEnabled.value = false
    }


    fun removeDataBase(database: QuestionDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentFile: File? = database.directory
                val targetName = database.name

                while (currentFile != null) {
                    val normalizedCurrent = currentFile.name.replace("-", " ").replace("_", " ")
                    val normalizedTarget = targetName.replace("-", " ").replace("_", " ")

                    if (normalizedCurrent.equals(normalizedTarget, ignoreCase = true)) {
                        Log.d("SELF", "MATCH FOUND (Normalized): ${currentFile.absolutePath}")
                        break
                    }
                    currentFile = currentFile.parentFile
                }

                if (currentFile != null) {
                    val success = currentFile.deleteRecursively()
                    if (success) {
                        withContext(Dispatchers.Main) {
                            databaseList.remove(database)
                        }
                        Log.d("SELF", "Successfully deleted: ${currentFile.name}")
                    }
                } else {
                    Log.d("SELF", "FAILURE: Could not find folder matching normalized name: $targetName")
                }
            } catch (e: Exception) {
                Log.e("SELF", "Error deleting database", e)
            }
        }
    }

    fun existingDatabases(context: Context) {
        val databaseDir = context.filesDir.resolve(databaseDirectoryName)

        if (!databaseDir.exists() || !databaseDir.isDirectory) {
            databaseDir.mkdirs()
        }

        databaseList.clear()

        val allFiles = databaseDir.listFiles()
        if (allFiles == null || allFiles.none { it.isDirectory }) {
            return
        }

        val sortedDirectories = allFiles
            .filter { it.isDirectory }
            .sortedByDescending { it.lastModified() }

        sortedDirectories.forEach { dir ->
            val contents = dir.listFiles()

            val isNotEmpty = contents != null && contents.isNotEmpty()

            if (isNotEmpty) {
                try {
                    val parser = TestFilesParser(dir.name, context)
                    val database = parser.getQuestionDatabase()

                    if (database.questions.isNotEmpty()) {
                        databaseList.add(database)
                    }
                } catch (e: Exception) {
                    android.util.Log.e("DatabaseImport", "Failed to parse: ${dir.name}", e)
                }
            } else {
                android.util.Log.w("DatabaseImport", "Skipping empty directory: ${dir.name}")
            }
        }
    }
}