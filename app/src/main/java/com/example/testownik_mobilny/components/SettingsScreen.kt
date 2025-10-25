package com.example.testownik_mobilny.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.AppSettings
import com.example.testownik_mobilny.FontSize
import com.example.testownik_mobilny.FontSize.LARGE
import com.example.testownik_mobilny.FontSize.MEDIUM
import com.example.testownik_mobilny.FontSize.SMALL
import com.example.testownik_mobilny.ui.theme.darkGray
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    dataStore: AppSettings,
    navigate: () -> Unit
){
//    FIXME: some kind of race condition bug
//    when you return from test page and immediately
//    go to settings application shows you gray background

    val appFontSize : FontSize by dataStore.fontSize.collectAsState(initial = MEDIUM)
    val usableFontSize = when(appFontSize){
        SMALL -> 16.sp
        MEDIUM -> 24.sp
        LARGE -> 32.sp
    }

    val fontSizes = remember { FontSize.entries.toList() }
    var selectedIndex = fontSizes.indexOf(appFontSize)

    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ){
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(15.dp)
        ) {
//            Title and return button
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.size(75.dp))
                Text(
                    "Settings",
                    fontFamily = jetBrainsMonoFontFamily,
                    fontSize = 32.sp,
//                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                RoundIconButton(
                    onClick = navigate,
                    icon = Icons.Filled.Clear
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
//            Containers for groups of specific settings
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(
                        darkGray,
                        RoundedCornerShape(10.dp)
                    )
            ) {
                SingleChoiceSegmentedButtonRow{
                    fontSizes.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = fontSizes.size
                            ),
                            onClick = {
                                selectedIndex = index
                                scope.launch{
                                    dataStore.setFontSize(size = label)
                                }
                            },
                            selected = index == selectedIndex,
                            label = {
                                Text(
                                    label.toString()
                                )
                            }
                        )
                    }
                }

                Text(
                    appFontSize.toString(),
                    fontSize = usableFontSize
                )
            }
        }
    }
}