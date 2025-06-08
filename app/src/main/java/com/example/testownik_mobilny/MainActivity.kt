package com.example.testownik_mobilny

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testownik_mobilny.Components.AddDataButton
import com.example.testownik_mobilny.Components.ImportFromLocalButton
import com.example.testownik_mobilny.Components.MainBody
import com.example.testownik_mobilny.ui.theme.TestownikMobilnyTheme
import com.example.testownik_mobilny.ui.theme.darkGray
import com.example.testownik_mobilny.Components.RoundIconButton
import com.example.testownik_mobilny.Components.TestButton
import com.example.testownik_mobilny.Components.TestInfoBar
import com.example.testownik_mobilny.Components.TestTopBar
import com.example.testownik_mobilny.Components.TopBar
import com.example.testownik_mobilny.TestLogic.QuestionDatabase
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.whitish
import kotlin.collections.forEach


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val mainViewModel = MainActivityViewModel()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestownikMobilnyTheme {
//                Navigation
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = "MainMenu"
                    ){
                        composable("MainMenu"){
                            MainMenu(
                                mainViewModel,
                                innerPadding,
                                navigate = { databaseIndex ->
                                    navController.navigate("TestScreen/$databaseIndex")
                                }
                            )
                        }
                        composable(
                            route = "TestScreen/{databaseIndex}",
                            arguments = listOf(navArgument("databaseIndex") { type = NavType.IntType })
                        ){ bse ->
                            val databaseIndex = bse.arguments?.getInt("databaseIndex") ?: 0
                            TestScreen(
                                innerPadding,
                                mainViewModel.databaseList.elementAt(databaseIndex),
                                navigate = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun TestScreen(innerPadding: PaddingValues, database: QuestionDatabase, navigate: () -> Unit){
        Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .padding(innerPadding)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    TestTopBar(
                        database.name,
                        goBack = navigate
                    )
                    TestInfoBar(database.numberOfQuestions.toString())
//                    Question box
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxHeight(0.3f).fillMaxWidth()
                    ){
                        Text(
//                            TODO: Add actively updated question index
                            "1. ${database.questions.first().question}",
                            fontSize = 20.sp,
                            fontFamily = jetBrainsMonoFontFamily,
                            textAlign = TextAlign.Center,
                        )
                    }

//                    Answers
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth()
                    ){
                        database.questions.first().answers.forEach { answer ->
                            Text(
                                answer,
                                fontSize = 16.sp,
                                fontFamily = jetBrainsMonoFontFamily
                            )
                        }
                    }

            }
        }
    }


    @Composable
    fun MainMenu(mainViewModel: MainActivityViewModel, innerPadding: PaddingValues, navigate: (Int) -> Unit){
        val context = LocalContext.current
        mainViewModel.existingDatabases(context)
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .padding(innerPadding)
            ) {
                Column {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
//                                Title of app, icon and settings button
                        TopBar(modifier = Modifier.align(Alignment.TopStart))

//                        Container for elements
                        MainBody(
                            modifier = Modifier.fillMaxSize().padding(top = 100.dp)
                        ){
                            GenerateDatabaseElements(mainViewModel.databaseList, navigate)
                        }

                        BottomGradient(Modifier.align(Alignment.BottomCenter))

//                                Bottom left dropdown menu
                        AddDataButton(modifier = Modifier.align(Alignment.BottomEnd)) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxHeight()
                            ) {
                                RoundIconButton(
                                    {},
                                    iconId = R.drawable.create_new,
                                    enabled = false
                                )
                                ImportFromLocalButton(
                                    viewModel = mainViewModel
                                )
                            }
                        }

                    }

            }
        }
    }

    @Composable
    fun BottomGradient(modifier: Modifier = Modifier){
        //                                Gradient effect at the end of the screen
        val gradient = Brush.verticalGradient(
            colors = listOf(darkGray.copy(0.01f), darkGray.copy(1f))
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(175.dp)
                .background(gradient)
        )
    }

    @Composable
    fun GenerateDatabaseElements(list: List<QuestionDatabase>,  navigate: (Int) -> Unit) {
        if (list.isNotEmpty()) {
            list.withIndex().forEach { (index, data) ->
                TestButton(data.name, data.numberOfQuestions, {navigate(index)})
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}