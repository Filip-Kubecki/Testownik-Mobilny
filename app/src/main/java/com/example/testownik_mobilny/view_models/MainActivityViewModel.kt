package com.example.testownik_mobilny.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.logic.TestFilesParser
import com.example.testownik_mobilny.ui.theme.databaseDirectoryName

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