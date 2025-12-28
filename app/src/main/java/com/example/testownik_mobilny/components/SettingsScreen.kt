package com.example.testownik_mobilny.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.AppSettings
import com.example.testownik_mobilny.FontSize
import com.example.testownik_mobilny.ui.theme.darkGray
import com.example.testownik_mobilny.ui.theme.debugOrange
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lightGray
import com.example.testownik_mobilny.ui.theme.whitish
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    dataStore: AppSettings,
    navigate: () -> Unit
) {
    val appFontSize by dataStore.fontSize.collectAsState(initial = FontSize.MEDIUM)
    val isDebugEnabled by dataStore.debugEnable.collectAsState(initial = false)

    val scope = rememberCoroutineScope()
    val fontSizes = remember { FontSize.entries.toList() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        color = MaterialTheme.colorScheme.background // Fixes the gray background race condition
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(48.dp)) // Balanced spacer
                Text(
                    "Settings",
                    fontFamily = jetBrainsMonoFontFamily,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                RoundIconButton(
                    onClick = navigate,
                    icon = Icons.Filled.Clear
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Settings Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkGray, RoundedCornerShape(16.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
//                  FONT SIZE - archive
//                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    Text(
//                        "Font Size",
//                        fontFamily = jetBrainsMonoFontFamily,
//                        color = whitish,
//                        fontSize = 18.sp
//                    )
//                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
//                        fontSizes.forEachIndexed { index, label ->
//                            SegmentedButton(
//                                shape = SegmentedButtonDefaults.itemShape(
//                                    index = index,
//                                    count = fontSizes.size
//                                ),
//                                onClick = {
//                                    scope.launch { dataStore.setFontSize(label) }
//                                },
//                                selected = label == appFontSize,
//                                label = { Text(label.name) }
//                            )
//                        }
//                    }
//                }
//
//                HorizontalDivider(color = whitish.copy(alpha = 0.1f))

                // DEBUG MODE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Debug Mode",
                            fontFamily = jetBrainsMonoFontFamily,
                            color = whitish,
                            fontSize = 18.sp
                        )
                        Text(
                            "Shows debug features",
                            color = whitish.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                    Switch(
                        checked = isDebugEnabled,
                        onCheckedChange = { newValue ->
                            scope.launch { dataStore.setDebugEnable(newValue) }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = debugOrange,
                            checkedTrackColor = debugOrange.copy(alpha = 0.2f),
                            checkedBorderColor = debugOrange,
                            checkedIconColor = Color.Black,

                            uncheckedThumbColor = lightGray,
                            uncheckedTrackColor = Color.Transparent,
                            uncheckedBorderColor = lightGray.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    }
}