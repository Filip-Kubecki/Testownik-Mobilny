package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.testownik_mobilny.ui.theme.darkGray

/**
 * Gradient at the end of the screen.
 * Creates the illusion of disappearing buttons near the bottom edge of the screen
 */
@Composable
fun BottomGradient(modifier: Modifier = Modifier){
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
