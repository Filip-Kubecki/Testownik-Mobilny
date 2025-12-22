package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.components.RoundIconButton
import com.example.testownik_mobilny.ui.theme.googleSansFlex
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.view_models.MainActivityViewModel

/**
 * Contains app title, icon and settings button
 */

// TODO: change to material 3 top bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    settingsNav: () -> Unit,
    mainViewModel: MainActivityViewModel,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Testownik",
                fontSize = 26.sp,
                fontFamily = googleSansFlex,
                color = Color.White
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.app_icon),
                contentDescription = "App Icon",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(45.dp)
            )
        },
        actions = {
            RoundIconButton(
                onClick = {
                    mainViewModel.disableSettings()
                    settingsNav.invoke()
                },
                modifier = Modifier.padding(end = 12.dp),
                enabled = mainViewModel.settingsEnabled.value,
                icon = Icons.Filled.Settings
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = lighterGray
        ),
        modifier = modifier
    )
}