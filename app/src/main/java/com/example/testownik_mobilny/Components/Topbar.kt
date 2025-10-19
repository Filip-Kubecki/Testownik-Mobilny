package com.example.testownik_mobilny.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lighterGray

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(lighterGray)
            .padding(bottom = 0.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = "Bulb",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(70.dp, 70.dp)
        )
        Text(
            text = "Testownik",
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            fontFamily = jetBrainsMonoFontFamily,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(0.5f)
        )

        RoundIconButton(
            onClick = {},
            modifier = Modifier,
            iconId = R.drawable.settings_icon
        )
    }
}