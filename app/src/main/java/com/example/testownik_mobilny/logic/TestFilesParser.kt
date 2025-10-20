package com.example.testownik_mobilny.logic

import android.content.Context
import android.util.Log
import java.io.File
import java.nio.charset.Charset

class TestFilesParser(
    private val uri: String,
    private val context: Context
) {
//    val databaseFolderUri: String = uri

    fun getQuestionDatabase(): QuestionDatabase{
        Log.d("SELF", "Started parsing: $uri")
        val questions = mutableListOf<Question>()

        val databaseDir = findDatabaseDirectory(File(context.filesDir, uri))
        var id = 1

//        Lists all files in database directory
        databaseDir?.listFiles()?.forEach { file ->
//            For txt files - parse them as questions
            if (file.extension == "txt"){
                val answers = mutableListOf<String>()
                val header = file.readLines(Charset.forName("Windows-1250"))[0]
                val correctAns = header.trim().drop(1).map{ it == '1' }

//                TODO: in future check if this encoding works with all databases
                file.readLines(Charset.forName("Windows-1250")).drop(2).forEach { line ->
                    if (line.isNotEmpty()) answers.add(line)
                }

                if (correctAns.size != answers.size){
                    Log.d("SELF", "Wrong format: ${file.name} ${header.drop(1)} ${correctAns.size} ${answers.size}")
                    return@forEach
                }

                questions.add(
                    Question(
                        id = id,
                        question = file.readLines(Charset.forName("Windows-1250"))[1],
                        answers = answers,
                        correctAnswers = correctAns
                    )
                )
                id++
            }
        }
        Log.d("SELF", "PARSED")

        return QuestionDatabase(uri, questions.size, questions, databaseDir)
    }

    private fun findDatabaseDirectory(directory: File?): File? {
        return if (folderContainsFiles(directory)){
            directory
        }else{
            findDatabaseDirectory(firstDirectory(directory))
        }
    }

    private fun folderContainsFiles(directory: File?): Boolean {
        return directory?.listFiles()?.any { it.isFile && it.extension == "txt" } == true
    }

    private fun firstDirectory(directory: File?): File? {
        return directory?.listFiles()?.firstOrNull { it.isDirectory }
    }
}