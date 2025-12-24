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

    fun existingDatabases(context: Context){
//        Directory for databases
        val databaseDir = context.filesDir.resolve(databaseDirectoryName)

//        Check if database directory exist - if not create it
        if (!databaseDir.exists() || !databaseDir.isDirectory){
            databaseDir.mkdirs()
        }

        databaseList.clear()
        if(databaseDir.listFiles()?.any{ it.isDirectory } == false){
            return
        }

//        Put all sub directories in list
        val directories = databaseDir.listFiles().filter { it.isDirectory }
//        TODO: make it safer. Check for empty folder and folders that don't match database criteria
        directories.forEach { dir ->
            val parser = TestFilesParser(dir.name.toString(), context)
            databaseList.add(parser.getQuestionDatabase())
        }
    }
}