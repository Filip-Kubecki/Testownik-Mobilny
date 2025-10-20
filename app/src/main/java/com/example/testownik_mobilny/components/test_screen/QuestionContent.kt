package com.example.testownik_mobilny.components.test_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import java.io.File

@Composable
fun QuestionContent(
    viewModel: TestScreenViewModel,
    directory: File?
){
    var regex = Regex("\\[img].*?\\[/img]", RegexOption.DOT_MATCHES_ALL)
    var containsImg = viewModel.currentQuestion.question.contains(regex)

    if (!containsImg){
        Text(
            "${viewModel.currentQuestion.id}. ${viewModel.currentQuestion.question}",
            fontSize = 18.sp,
            fontFamily = jetBrainsMonoFontFamily,
            textAlign = TextAlign.Center,
        )
    }else{

        var src = regex.find(viewModel.currentQuestion.question)
        var img = src?.groupValues?.get(0)?.replace("[img]", "")?.replace("[/img]","")

        Log.d("SELF", "There is image with source: ${src?.groupValues?.get(0)?.replace("[img]", "")
            ?.replace("[/img]","")}")


        val file = File(directory, img)
        Log.d("SELF", "${file.path}")
        Image(
            rememberAsyncImagePainter(file),
            contentDescription = "img",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}