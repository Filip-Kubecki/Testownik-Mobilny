package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.testownik_mobilny.components.RoundIconButton
import com.example.testownik_mobilny.view_models.MainActivityViewModel
import com.example.testownik_mobilny.R

@Composable
fun MainMenuScreen(mainViewModel: MainActivityViewModel, innerPadding: PaddingValues, navigate: (Int) -> Unit){
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
//              Title of app, icon and settings button
                TopBar(modifier = Modifier.align(Alignment.TopStart))

//              Container for elements
                ImportedDatabaseContainer(
                    modifier = Modifier.fillMaxSize().padding(top = 100.dp)
                ){
                    GenerateDatabaseElements(mainViewModel.databaseList, navigate)
                }

                BottomGradient(Modifier.align(Alignment.BottomCenter))

//              Bottom left dropdown menu
                PopUpButton(modifier = Modifier.align(Alignment.BottomEnd)) {
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
