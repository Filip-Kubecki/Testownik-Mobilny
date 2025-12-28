package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.components.RemoveDatabaseDialog
import com.example.testownik_mobilny.components.TestInfoSheet
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.logic.TestInfoManager
import com.example.testownik_mobilny.ui.theme.darkGray
import com.example.testownik_mobilny.ui.theme.googleSansFlex
import com.example.testownik_mobilny.ui.theme.lightGray
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.removeRed
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.ui.theme.winnerGold
import com.example.testownik_mobilny.view_models.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Given list with [QuestionDatabase] and [androidx.navigation.NavController] generates list of buttons
 * corresponding to each database. Buttons navigate to [com.example.testownik_mobilny.components.test_screen.TestScreen]
 * with data of chosen database
 */
fun LazyListScope.generateDatabaseElements(
    mainViewModel: MainActivityViewModel,
    list: List<QuestionDatabase>,
    navigate: (Int) -> Unit
) {
    itemsIndexed(
        list,
        key = { _, data -> data.directory?.absolutePath ?: data.name }
    ) { index, data ->
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(data.directory?.absolutePath) {
            visible = true
        }

        val slideDuration = 800
        val staggerDelay = 350
        val fadeDuration = 1000

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                animationSpec = tween(
                    durationMillis = slideDuration,
                    delayMillis = index * staggerDelay
                ),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = fadeDuration,
                    delayMillis = index * staggerDelay
                )
            )
        ) {
            DatabaseCard(
                name = data.name,
                questionCount = data.numberOfQuestions,
                viewmodel = mainViewModel,
                database = data,
                onClick = { navigate(index) }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatabaseCard(
    name: String,
    questionCount: Int,
    viewmodel: MainActivityViewModel,
    database: QuestionDatabase,
    onClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showInfoSheet by remember { mutableStateOf(false) }

    var progress by remember { mutableFloatStateOf(0f) }
    val isFinished = progress >= 1f

    LaunchedEffect(database) {
        val info = withContext(Dispatchers.IO) {
            TestInfoManager.loadTestInfo(database.directory!!)
        }
        info?.let {
            progress = if (it.numberOfQuestions > 0)
                it.memorizedQuestions.size.toFloat() / it.numberOfQuestions
            else 0f
        }
    }

    OutlinedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = lightGray.copy(alpha = 0.05f),
            contentColor = whitish
        ),
        border = BorderStroke(
            width = 1.dp,
            color = whitish.copy(alpha = if (isFinished) 0.5f else 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = googleSansFlex,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (isFinished) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = "Finished",
                            tint = winnerGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = lightGray
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        shape = RoundedCornerShape(20.dp),
                        containerColor = darkGray,
                        modifier = Modifier
                            .width(180.dp)
                            .border(
                                width = 1.dp,
                                color = lightGray.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        DropdownMenuItem(
                            text = { Text("Info", fontSize = 15.sp, fontFamily = googleSansFlex) },
                            onClick = {
                                showInfoSheet = true
                                menuExpanded = false
                            },
                            leadingIcon = {
                                Icon(Icons.Outlined.Info, contentDescription = null, modifier = Modifier.size(20.dp))
                            }
                        )

                        DropdownMenuItem(
                            enabled = false,
                            text = {
                                Column(verticalArrangement = Arrangement.spacedBy((-4).dp)) {
                                    Text("Edit", fontSize = 15.sp, fontFamily = googleSansFlex)
                                    Text("Coming Soon", fontSize = 11.sp, fontFamily = googleSansFlex, color = whitish.copy(alpha = 0.5f))
                                }
                            },
                            onClick = {/* TODO: add edit functionality here */},
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier
                            .padding(vertical = 4.dp),
                            thickness = 1.dp,
                            color = lightGray.copy(alpha = 0.1f)
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Remove",
                                    fontSize = 15.sp,
                                    color = removeRed,
                                    fontFamily = googleSansFlex
                                )
                            },
                            onClick = {
                                showDeleteDialog = true
                                menuExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = null,
                                    tint = removeRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // PROGRESS SECTION
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (isFinished) positiveGreen else whitish.copy(alpha = 0.4f),
                    trackColor = whitish.copy(alpha = 0.1f),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Questions: $questionCount",
                        fontSize = 13.sp,
                        color = lightGray,
                        fontFamily = googleSansFlex
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFinished) positiveGreen else lightGray,
                        fontFamily = googleSansFlex
                    )
                }
            }
        }

        if (showDeleteDialog) {
            RemoveDatabaseDialog(database, viewmodel) { showDeleteDialog = false }
        }

        if (showInfoSheet) {
            TestInfoSheet(
                database = database,
                onDismiss = { showInfoSheet = false },
                onStartTest = {
                    showInfoSheet = false
                    onClick()
                }
            )
        }
    }
}