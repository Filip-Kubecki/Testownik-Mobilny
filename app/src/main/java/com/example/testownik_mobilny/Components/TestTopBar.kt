package com.example.testownik_mobilny.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lighterGray

@Composable
fun TestTopBar(
    testName: String,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(lighterGray)
            .padding(bottom = 0.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.menu_icon),
            contentDescription = "SideMenu",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(40.dp, 40.dp)
        )
        Text(
            text = testName,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontFamily = jetBrainsMonoFontFamily,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(250.dp)
        )

        RoundIconButton(
            onClick = goBack,
            modifier = Modifier.size(80.dp, 80.dp),
            iconId = R.drawable.plus_icon,
            rotation = 45f
        )
    }
}
