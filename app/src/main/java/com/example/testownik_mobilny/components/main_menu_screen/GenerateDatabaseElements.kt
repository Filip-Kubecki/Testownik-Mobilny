package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
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
    if (list.isNotEmpty()) {
        list.withIndex().forEach { (index, data) ->
            TestButton(data.name, data.numberOfQuestions, {navigate(index)})
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}