package com.example.testownik_mobilny.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.testownik_mobilny.components.test_screen.formatTime
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.logic.TestInfo
import com.example.testownik_mobilny.logic.TestInfoManager
import com.example.testownik_mobilny.ui.theme.googleSansFlex
import com.example.testownik_mobilny.ui.theme.mediumGray
import com.example.testownik_mobilny.ui.theme.positiveGreen
import com.example.testownik_mobilny.ui.theme.undecidedYellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestInfoSheet(
    database: QuestionDatabase,
    onDismiss: () -> Unit,
    onStartTest: () -> Unit
) {

//    TODO: maybe in the future change "Powtórz materiał" button functionality
//          so it resets test data so you can start again
    var testInfo by remember { mutableStateOf<TestInfo?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(database) {
        val loaded = withContext(Dispatchers.IO) {
            TestInfoManager.loadTestInfo(database.directory!!)
        }

        testInfo = loaded
            ?: TestInfo(
                name = database.name,
                numberOfQuestions = database.numberOfQuestions
            ).apply {
                repeat(database.numberOfQuestions) { index ->
                    unvisitedQuestions.add(index)
                }
            }
        isLoading = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    CircularProgressIndicator()
                }
            } else {
                testInfo?.let { info ->
                    val progress = if (info.numberOfQuestions > 0)
                        info.memorizedQuestions.size.toFloat() / info.numberOfQuestions else 0f

                    val isFinished = progress >= 1f

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = info.name,
                            style = MaterialTheme.typography.headlineMedium.copy(fontFamily = googleSansFlex)
                        )
                        if (isFinished) {
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                Icons.Default.Celebration,
                                contentDescription = null,
                                tint = undecidedYellow
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Całkowity spędzony czas: ${formatTime(info.timeSpent)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = googleSansFlex),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isFinished) "Materiał Opanowany!" else "Postęp nauki",
                            style = MaterialTheme.typography.labelLarge.copy(fontFamily = googleSansFlex),
                            color = if (isFinished) positiveGreen else MaterialTheme.colorScheme.onSurface
                        )
                        if (isFinished) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = positiveGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    StackedProgressBar(
                        memorized = info.memorizedQuestions.size,
                        answered = info.answeredQuestions.size,
                        unvisited = info.unvisitedQuestions.size,
                        isFinished = isFinished
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    InfoStatRow(
                        "Zapamiętane",
                        info.memorizedQuestions.size,
                        info.numberOfQuestions,
                        positiveGreen
                    )
                    InfoStatRow(
                        "W trakcie",
                        info.answeredQuestions.size,
                        info.numberOfQuestions,
                        undecidedYellow
                    )
                    InfoStatRow(
                        "Nieodwiedzone",
                        info.unvisitedQuestions.size,
                        info.numberOfQuestions,
                        mediumGray
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            onStartTest()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = if (isFinished)
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        else ButtonDefaults.buttonColors()
                    ) {
                        Text(
                            text = if (isFinished) "Powtórz materiał" else "Rozpocznij Test",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = googleSansFlex,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StackedProgressBar(
    memorized: Int,
    answered: Int,
    unvisited: Int,
    isFinished: Boolean
) {
    val total = (memorized + answered + unvisited).toFloat()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isFinished) 12.dp else 8.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (memorized > 0) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(memorized / total)
                    .background(positiveGreen)
            )
        }
        if (answered > 0) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(answered / total)
                    .background(undecidedYellow)
            )
        }
        if (unvisited > 0) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(unvisited / total)
                    .background(mediumGray)
            )
        }
    }
}

@Composable
private fun InfoStatRow(
    label: String,
    count: Int,
    total: Int,
    color: Color
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(12.dp).background(color, CircleShape))
            Spacer(Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = googleSansFlex)
            )
        }
        Text(
            text = "$count / $total",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = googleSansFlex
            )
        )
    }
}