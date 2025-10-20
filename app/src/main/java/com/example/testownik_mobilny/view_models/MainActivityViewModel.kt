package com.example.testownik_mobilny.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.logic.TestFilesParser

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivityViewModel: ViewModel() {
    var databaseList = mutableStateListOf<QuestionDatabase>()
        private set

    fun existingDatabases(context: Context){
        databaseList.clear()
        if(context.filesDir.listFiles()?.any{ it.isDirectory } == false){
            return
        }

        val directories = context.filesDir.listFiles().filter { it.isDirectory }

        directories.forEach { dir ->
            val parser = TestFilesParser(dir.name.toString(), context)
            databaseList.add(parser.getQuestionDatabase())
        }
    }
}