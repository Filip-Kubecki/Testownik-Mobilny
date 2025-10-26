package com.example.testownik_mobilny.components.test_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.testownik_mobilny.ui.theme.darkGray
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.view_models.TestScreenViewModel

/**
 * Let's user check their answers and navigate to next question
 */
@Composable
fun ConfirmChoiceButton(
    viewModel: TestScreenViewModel,
    modifier: Modifier = Modifier,
    iconDescription: String = "Icon",
) {
    var isToggled by remember { mutableStateOf(false) }
    val targetIcon : ImageVector =  if (!isToggled) Icons.Filled.Check else Icons.Filled.PlayArrow

    IconButton(
        onClick = {
            if (isToggled == false) {
                isToggled = true
                viewModel.checkAnswers()
            } else {
                isToggled = false
                viewModel.confirmButtonCheck()
                viewModel.resetContentImageVisibility()
            }
        },
        enabled = !viewModel.finishedScreenState,
        colors = IconButtonColors(
            containerColor = positiveGreen,
            contentColor = whitish,
            disabledContentColor = darkGray,
            disabledContainerColor = darkGray
        ),
        modifier = modifier
            .size(75.dp)
    ) {
        AnimatedContent(
            targetState = targetIcon,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn(animationSpec = tween(300)))
                    .togetherWith(slideOutVertically { height -> -height } + fadeOut(animationSpec = tween(300)))
                    .using(SizeTransform(clip = false))
            },
            label = ""
        ) { iconVector ->
            Icon(
                imageVector = iconVector,
                iconDescription,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
