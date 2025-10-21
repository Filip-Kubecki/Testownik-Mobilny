package com.example.testownik_mobilny.components.test_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.testownik_mobilny.removeImgTag
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import java.io.File

@Composable
fun QuestionContent(
    viewModel: TestScreenViewModel,
    directory: File?
){
//    Regex finding img sources in between [img] tags
    var regex = Regex("\\[img].*?\\[/img]", RegexOption.DOT_MATCHES_ALL)
    var containsImg = viewModel.currentQuestion.question.contains(regex)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (containsImg){
            var src = regex.find(viewModel.currentQuestion.question)
            val srcString = src?.groupValues?.get(0).toString()
            var img = removeImgTag(srcString)
            val file = File(directory, img)
//            val dispText = viewModel.currentQuestion.question.replace(srcString, "")
//            Text(
//                "${viewModel.currentQuestion.id}. $dispText",
//                fontSize = 18.sp,
//                fontFamily = jetBrainsMonoFontFamily,
//                textAlign = TextAlign.Start,
//            )
//            TODO: Issue with scaling all types of pictures properly.
//                  Long pictures crops out sides or are to small to read.
//                  Big pictures have problem with
            Image(
                rememberAsyncImagePainter(file),
                contentDescription = "img",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 70.dp, max = 300.dp)
            )
        }else{
            Text(
                "${viewModel.currentQuestion.id}. ${viewModel.currentQuestion.question}",
                fontSize = 18.sp,
                fontFamily = jetBrainsMonoFontFamily,
                textAlign = TextAlign.Justify,
            )
        }
    }

}