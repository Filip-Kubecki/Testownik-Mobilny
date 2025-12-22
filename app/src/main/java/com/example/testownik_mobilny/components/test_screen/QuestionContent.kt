@file:Suppress("LABEL_NAME_CLASH")

package com.example.testownik_mobilny.components.test_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.testownik_mobilny.removeImgTag
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.view_models.TestScreenViewModel
import java.io.File

@Composable
fun QuestionContent(
    viewModel: TestScreenViewModel,
    directory: File?,
){
//    Regex finding img sources in between [img] tags
    val regex = Regex("\\[img].*?\\[/img]", RegexOption.DOT_MATCHES_ALL)
    val containsImg = viewModel.currentQuestion.question.contains(regex)

    val density = LocalDensity.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (containsImg){
            val src = regex.find(viewModel.currentQuestion.question)
            val srcString = src?.groupValues?.get(0).toString()
            val img = removeImgTag(srcString)
            val file = File(directory, img)

            Column(modifier = Modifier.fillMaxWidth()){
                this@Column.AnimatedVisibility(
                    viewModel.questionContentImageVisibility,
                    enter = slideInVertically{
                        with(density){ -40.dp.roundToPx()}
                    } + expandVertically(
                        expandFrom = Alignment.Top
                    )+ fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
//                    TODO: Make image zoomable - eg: on double tap fullscreen image
                    Image(
                        rememberAsyncImagePainter(file),
                        contentDescription = "img",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .heightIn(min = 50.dp)
                            .fillMaxWidth()
                            .background(whitish) // Added because some .png don't have background
                    )
                }
                IconButton(
                    {viewModel.changeContentImageVisibility()},
                    modifier = Modifier
                        .align(Alignment.End)
                        .background(lighterGray)
                        .size(width = 40.dp, height = 32.dp)
                ) {
                    Icon(
                        if(viewModel.questionContentImageVisibility){
                            Icons.Filled.KeyboardArrowUp
                        }else{
                            Icons.Filled.KeyboardArrowDown
                        },
                        "Collapse or expand question image"
                    )
                }
            }
        }else{
            Text(
                "${viewModel.currentQuestion.id}. ${viewModel.currentQuestion.question}",
                fontSize = 18.sp,
                fontFamily = jetBrainsMonoFontFamily,
                textAlign = TextAlign.Justify,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

}