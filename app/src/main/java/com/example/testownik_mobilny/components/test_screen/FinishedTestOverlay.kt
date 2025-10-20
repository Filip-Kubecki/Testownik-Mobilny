package com.example.testownik_mobilny.components.test_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.ui.theme.lighterGray

@Composable
fun FinishedTestOverlay(

    goBack : () -> Unit
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(0.7f))
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .fillMaxWidth(0.85f)
                .background(lighterGray, RoundedCornerShape(25.dp))
        ) {
            Text(
                "Nauczyłeś się wszystkich pytań!",
                fontSize = 24.sp
            )
            Text(
                "Gratulacje!",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(goBack) {
                Text(
                    "Wracamy!",
                    fontSize = 32.sp
                    )
            }
        }
    }
}