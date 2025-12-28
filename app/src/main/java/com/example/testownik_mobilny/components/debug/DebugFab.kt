package com.example.testownik_mobilny.components.debug

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import kotlinx.coroutines.launch


@Composable
fun DebugFab(
    onClick: () -> Unit,
    color: Color,
    icon: ImageVector,
    label: String
) {
    val scope = rememberCoroutineScope()

    val scale = remember { Animatable(1f) }
    val offsetX = remember { Animatable(0f) }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                translationX = offsetX.value
            },
        contentAlignment = Alignment.TopCenter
    ) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    launch {
                        scale.animateTo(0.97f, animationSpec = tween(40))
                        scale.animateTo(1f, animationSpec = spring(dampingRatio = 0.6f))
                    }

                    val shakeSpec = tween<Float>(40)
                    offsetX.animateTo(2f, shakeSpec)
                    offsetX.animateTo(-2f, shakeSpec)
                    offsetX.animateTo(0f, shakeSpec)

                    onClick()
                }
            },
            containerColor = Color.Black,
            contentColor = color,
            shape = CircleShape,
            modifier = Modifier
                .size(64.dp)
                .border(2.dp, color, CircleShape)
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(32.dp))
        }

        Surface(
            color = color,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.offset(y = (-10).dp)
        ) {
            Text(
                text = "DBG $label",
                color = Color.Black,
                fontSize = 9.sp,
                fontFamily = jetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}