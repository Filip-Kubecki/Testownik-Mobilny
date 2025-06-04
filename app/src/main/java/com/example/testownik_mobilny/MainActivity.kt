package com.example.testownik_mobilny

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import com.example.testownik_mobilny.Components.AddDataButton
import com.example.testownik_mobilny.Components.ImportFromLocalButton
import com.example.testownik_mobilny.ui.theme.TestownikMobilnyTheme
import com.example.testownik_mobilny.ui.theme.darkGray
import kotlinx.coroutines.launch
import java.io.File
import com.example.testownik_mobilny.Components.RoundIconButton
import com.example.testownik_mobilny.Components.TestButton
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lightGray
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.whitish


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestownikMobilnyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(1f)
                            .padding(innerPadding)
                    ) {
                        Column {
                            val fileCount = LocalContext.current.filesDir.listFiles().size
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                TopBar(modifier = Modifier.align(Alignment.TopStart))
                                BodyContent(
                                    modifier = Modifier.fillMaxSize().padding(top = 100.dp)
                                )

//                                Gradient effect
                                val gradient = Brush.verticalGradient(
                                    colors = listOf(darkGray.copy(0.01f), darkGray.copy(1f))
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .background(gradient)
                                        .align(Alignment.BottomCenter)
                                )
//                                Bottom left dropdown menu
                                AddDataButton(modifier = Modifier.align(Alignment.BottomEnd)) {
                                    Column(
                                        verticalArrangement = Arrangement.SpaceEvenly,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                    ) {
                                        RoundIconButton(
                                            {},
                                            iconId = R.drawable.create_new
                                        )
                                        ImportFromLocalButton()
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TopBar(modifier: Modifier = Modifier) {
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
                painter = painterResource(R.drawable.app_icon),
                contentDescription = "Bulb",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(70.dp, 70.dp)
            )
            Text(
                text = "Testownik",
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                fontFamily = jetBrainsMonoFontFamily,
                color = Color.White,
                modifier = Modifier.width(250.dp)
            )

            RoundIconButton(
                onClick = {},
                modifier = Modifier,
                iconId = R.drawable.settings_icon
            )
        }
    }

    @Composable
    fun BodyContent(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .background(darkGray)
                .fillMaxWidth()
                .padding(top = 0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            val divH = 15.dp
//            FilePicker()
            Spacer(modifier = Modifier.height(divH))
            Text("Ostatnie:",
                color = lightGray,
                fontFamily = jetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.height(divH*0.6f))
            repeat(20) {
                TestButton("Podstawy metro", 34, {})
                Spacer(modifier = Modifier.height(divH))
            }
        }
    }
}