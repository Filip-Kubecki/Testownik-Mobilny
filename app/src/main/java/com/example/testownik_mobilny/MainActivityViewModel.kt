package com.example.testownik_mobilny

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.testownik_mobilny.Components.TestButton
import com.example.testownik_mobilny.TestLogic.QuestionDatabase
import com.example.testownik_mobilny.TestLogic.TestFilesParser

class MainActivityViewModel: ViewModel() {
    var uri by mutableStateOf("".toUri())
        private set

    var databaseList = mutableStateSetOf<QuestionDatabase>()
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