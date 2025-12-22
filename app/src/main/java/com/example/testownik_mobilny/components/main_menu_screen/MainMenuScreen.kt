package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import com.example.testownik_mobilny.AppSettings
import com.example.testownik_mobilny.view_models.MainActivityViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainMenuScreen(
    mainViewModel: MainActivityViewModel,
    dataStore: AppSettings,
    navigate: (Int) -> Unit,
    settingsNav: () -> Unit
){
    val context = LocalContext.current
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val dimAlpha by animateFloatAsState(
        targetValue = if (fabMenuExpanded) 0.6f else 0f,
        animationSpec = tween(durationMillis = 250),
        label = "DimAlpha"
    )

    val fabVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 || !listState.canScrollForward
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.existingDatabases(context)
        delay(200)
        mainViewModel.toggleSettings()
    }

    Scaffold(
        topBar = {
            TopBar(
                settingsNav,
                mainViewModel
            )
        },
        floatingActionButton = {
            MainMenuFAB(
                viewModel = mainViewModel,
                isVisible = fabVisible,
                expanded = fabMenuExpanded,
                onExpandedChange = { fabMenuExpanded = it }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Column(modifier = Modifier.fillMaxSize()) {
                ImportedDatabaseContainer(
                    modifier = Modifier.weight(1f)
                ) {
                    generateDatabaseElements(mainViewModel.databaseList, navigate)
                }
            }

            if (dimAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = dimAlpha))
                        .zIndex(2f)
                        .clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null
                        ) { fabMenuExpanded = false }
                )
            }
        }
    }
}
