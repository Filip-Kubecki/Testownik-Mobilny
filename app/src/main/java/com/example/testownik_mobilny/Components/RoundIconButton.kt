package com.example.testownik_mobilny.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.ui.theme.lighterGray

@Composable
fun RoundIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconId: Int = R.drawable.app_icon,
    iconDescription: String = "Icon",
    rotation: Float = 0f,
    enabled: Boolean = true
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = lighterGray
        ),
        enabled = enabled,
        modifier = modifier
            .size(75.dp, 75.dp)
    ){
        Image(
            painter = painterResource(iconId),
            contentDescription = iconDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.rotate(rotation)
        )
    }
}