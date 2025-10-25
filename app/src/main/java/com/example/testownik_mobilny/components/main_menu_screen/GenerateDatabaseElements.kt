package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testownik_mobilny.logic.QuestionDatabase

/**
 * Given list with [QuestionDatabase] and [androidx.navigation.NavController] generates list of buttons
 * corresponding to each database. Buttons navigate to [com.example.testownik_mobilny.components.test_screen.TestScreen]
 * with data of chosen database
 */
@Composable
fun GenerateDatabaseElements(list: List<QuestionDatabase>,  navigate: (Int) -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    if (list.isNotEmpty()) {
        list.withIndex().forEach { (index, data) ->
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(
                    animationSpec = tween(250*index+100),
                    initialOffsetX = {fullWidth -> -fullWidth}
                ) + fadeIn(
                    animationSpec = tween(750*index+100)
                )
            ) {
                TestButton(data.name, data.numberOfQuestions, {navigate(index)})
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}