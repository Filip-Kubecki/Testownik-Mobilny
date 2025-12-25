package com.example.testownik_mobilny.components.test_screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import kotlinx.coroutines.delay

@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TestScreen(
    database: QuestionDatabase,
    navigate: () -> Unit,
    settingsNav: () -> Unit
){
    val localViewModel = TestScreenViewModel()

    LaunchedEffect(Unit){
        localViewModel.init(database)
        delay(500)
    }

    Scaffold(
        topBar = {
            TestTopBar(
                database.name,
                goBack = navigate
            )
        },
        floatingActionButton = {
            ConfirmChoiceButton(
                localViewModel
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
//            .padding(innerPadding)
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
//              Info about current test
                TestInfoBar(localViewModel)

//              Question box
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                        .padding(
                            top = 10.dp,
                            bottom = 10.dp,
                            start = 20.dp,
                            end = 20.dp
                        )
                ){
                    QuestionContent(
                        localViewModel,
                        database.directory
                    )
                }
//                    Answers
//              TODO: scale content dynamically
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxHeight(0.95f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                ){
                    localViewModel.shuffledAnswerIndices.forEachIndexed { visualIndex, originalIndex ->
                        val rawAnswer = localViewModel.currentQuestion.answers[originalIndex]

//                        val cleanedAnswer = rawAnswer
//                            .replace(Regex("^[a-zA-Z0-9][).-]\\s*"), "")
//                            .trim()

//                        val autoLabel = ('a' + visualIndex).toString() + ") "

                        TestAnswerButton(
//                            answer = autoLabel + cleanedAnswer, -- better way TODO: change to this in final version
                            answer = rawAnswer, // better for debug
                            id = originalIndex,
                            viewModel = localViewModel,
                            directory = database.directory,
                            debugMode = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ){

            }


            if (localViewModel.finishedScreenState){
                FinishedTestOverlay(navigate)
            }
        }
    }
}
