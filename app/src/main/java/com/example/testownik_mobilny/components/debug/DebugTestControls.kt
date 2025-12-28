package com.example.testownik_mobilny.components.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.negativeRed
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.view_models.TestScreenViewModel

@Composable
fun DebugTestControls(
    localViewModel: TestScreenViewModel,
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 16.dp, bottom = 10.dp)
            .width(110.dp)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Color.Black.copy(alpha = 0.95f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "DEBUG TOOLS",
                fontFamily = jetBrainsMonoFontFamily,
                fontSize = 12.sp,
                color = whitish.copy(alpha = 0.5f),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp).width(40.dp),
                thickness = 1.dp,
                color = whitish.copy(alpha = 0.2f)
            )
        }

        // Force Correct Answer
        DebugFab(
            onClick = { localViewModel.debugForceCorrect() },
            color = positiveGreen,
            icon = Icons.Default.Check,
            label = "CORRECT"
        )

        // Force Wrong Answer
        DebugFab(
            onClick = { localViewModel.debugForceWrong() },
            color = negativeRed,
            icon = Icons.Default.Close,
            label = "WRONG"
        )
        Divider(modifier = Modifier.height(4.dp))
    }
}