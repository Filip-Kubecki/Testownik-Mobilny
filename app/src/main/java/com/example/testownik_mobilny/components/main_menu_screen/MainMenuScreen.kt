package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.testownik_mobilny.AppSettings
import com.example.testownik_mobilny.components.RoundIconButton
import com.example.testownik_mobilny.view_models.MainActivityViewModel
import kotlinx.coroutines.delay

@Composable
fun MainMenuScreen(
    mainViewModel: MainActivityViewModel,
    innerPadding: PaddingValues,
    dataStore: AppSettings,
    navigate: (Int) -> Unit,
    settingsNav: () -> Unit
){
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(200)
        mainViewModel.toggleSettings()
    }

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
                TopBar(
                    settingsNav,
                    mainViewModel,
                    modifier = Modifier.align(Alignment.TopStart)
                )

//              Container for elements
                ImportedDatabaseContainer(
                    modifier = Modifier.fillMaxSize().padding(top = 100.dp)
                ){
                    GenerateDatabaseElements(
                        mainViewModel.databaseList,
                        navigate
                    )
                }


                BottomGradient(Modifier.align(Alignment.BottomCenter))

//              Bottom left dropdown menu
                this@Column.AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(
                        animationSpec = tween(800),
                        initialOffsetY = {fullHeight -> fullHeight}
                    ) + fadeIn(
                        animationSpec = tween(800)
                    )
                ) {
                    PopUpButton(modifier = Modifier.align(Alignment.BottomEnd)) {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxHeight()
                        ) {
                            RoundIconButton(
                                {},
                                icon = Icons.Filled.Create,
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
}
