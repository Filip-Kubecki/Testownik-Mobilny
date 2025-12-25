package com.example.testownik_mobilny.components.test_screen

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.components.RoundIconButton
import com.example.testownik_mobilny.ui.theme.googleSansFlex
import com.example.testownik_mobilny.ui.theme.lighterDarkGray

/**
 * Contains test database name, return button and option button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTopBar(
    testName: String,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
    title = {
        Text(
            text = testName,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontFamily = googleSansFlex,
            color = Color.White,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    initialDelayMillis = 10000,
                    repeatDelayMillis = 5000,
                    velocity = 30.dp
                ),
            overflow = TextOverflow.Ellipsis
        )
    },
    navigationIcon = {
        RoundIconButton(
            onClick = goBack,
            icon = Icons.Filled.Close,
        )
    },
    actions = {
        RoundIconButton(
            {}, // TODO: add this functionality - additional uses of test screen
            icon = Icons.Filled.Menu,
            iconDescription = "Side menu"
        )
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = lighterDarkGray
    ),
    modifier = modifier
    )
}
