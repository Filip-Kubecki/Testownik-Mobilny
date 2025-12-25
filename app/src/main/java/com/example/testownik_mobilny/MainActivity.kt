package com.example.testownik_mobilny

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testownik_mobilny.components.SettingsScreen
import com.example.testownik_mobilny.components.main_menu_screen.MainMenuScreen
import com.example.testownik_mobilny.components.test_screen.TestScreen
import com.example.testownik_mobilny.ui.theme.TestownikMobilnyTheme
import com.example.testownik_mobilny.view_models.MainActivityViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val mainViewModel = MainActivityViewModel()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestownikMobilnyTheme {
//              Navigation
                val navController = rememberNavController()
                val dataStore = AppSettings(LocalContext.current.applicationContext)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = "MainMenu" // Default destination
                    ){
//                        MAIN MENU SCREEN
                        composable(
                            route = "MainMenu"
                        ){
                            MainMenuScreen(
                                mainViewModel,
                                dataStore,
                                navigate = { databaseIndex ->
                                    navController.navigate("TestScreen/$databaseIndex")
                                },
                                settingsNav = {
                                    navController.navigate("Settings")
                                }
                            )
                        }

//                        SETTINGS SCREEN
                        composable(
                            route = "Settings"
                        ){
                            SettingsScreen(
                                innerPadding,
                                dataStore,
                                navigate = {
                                    navController.popBackStack()
                                }
                            )
                        }

//                        TEST SCREEN
                        composable(
                            route = "TestScreen/{databaseIndex}",
                            arguments = listOf(navArgument("databaseIndex") { type = NavType.IntType })
                        ){ bse ->
                            val databaseIndex = bse.arguments?.getInt("databaseIndex") ?: 0
                            TestScreen(
                                mainViewModel.databaseList.elementAt(databaseIndex),
                                navigate = {
                                    navController.popBackStack()
                                },
                                settingsNav = {
                                    navController.navigate("Settings")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}