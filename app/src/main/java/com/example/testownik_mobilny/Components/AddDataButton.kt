package com.example.testownik_mobilny.Components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.ui.theme.darkGray
import com.example.testownik_mobilny.ui.theme.lightGray
import com.example.testownik_mobilny.ui.theme.lighterGray

@Composable
fun AddDataButton(
    modifier: Modifier = Modifier,
    initiallyOpen: Boolean = false,
    animationTime: Int = 500,
    content: @Composable () -> Unit
){
    var isOpen by remember {
        mutableStateOf(initiallyOpen)
    }
    val alpha = animateFloatAsState(
        targetValue = if(isOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationTime
        )
    )
    val rotation = animateFloatAsState(
        targetValue = if(isOpen) 0f else 90f,
        animationSpec = tween(
            durationMillis = animationTime
        )
    )
    val iconRotation = animateFloatAsState(
        targetValue = if(isOpen) 45f else 0f,
        animationSpec = tween(
            durationMillis = (animationTime*0.8).toInt(),
            easing = EaseInOut
        )
    )

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
            .background(darkGray.copy(alpha.value*0.6f))
            .fillMaxSize()
            .offset((-20).dp, (-20).dp)
    ) {
        Box(
            modifier = Modifier
                .background(lighterGray, shape = CircleShape)
                .align(Alignment.BottomEnd)
        ) {
            RoundIconButton(
                {isOpen = !isOpen},
                modifier = Modifier.border(2.dp, lightGray.copy(0.5f), CircleShape),
                iconId = R.drawable.plus_icon,
                rotation = iconRotation.value
            )
        }
        if (isOpen){
            Box(
                modifier = Modifier
                    .height(150.dp+50.dp)
                    .zIndex(1f) // Draw on top
                    .absoluteOffset(0.dp, (-75).dp)
            ) {
                Box(
                    modifier = Modifier
                        .graphicsLayer{
                            transformOrigin = TransformOrigin(0f, 1f)
                            rotationX = rotation.value
                        }
                        .alpha(alpha.value)
                ) {
                    content()
                }
            }
        }
    }
}