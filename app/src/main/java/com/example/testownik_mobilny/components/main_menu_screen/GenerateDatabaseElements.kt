package com.example.testownik_mobilny.components.main_menu_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.ui.theme.darkGray
import com.example.testownik_mobilny.ui.theme.jetBrainsMonoFontFamily
import com.example.testownik_mobilny.ui.theme.lightGray
import com.example.testownik_mobilny.ui.theme.negativeRed
import com.example.testownik_mobilny.ui.theme.whitish

/**
 * Given list with [QuestionDatabase] and [androidx.navigation.NavController] generates list of buttons
 * corresponding to each database. Buttons navigate to [com.example.testownik_mobilny.components.test_screen.TestScreen]
 * with data of chosen database
 */
fun LazyListScope.generateDatabaseElements(
    list: List<QuestionDatabase>,
    navigate: (Int) -> Unit
) {
    itemsIndexed(list) { index, data ->
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                animationSpec = tween(250 * index + 100),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) + fadeIn(animationSpec = tween(750 * index + 100))
        ) {
            DatabaseCard(
                name = data.name,
                questionCount = data.numberOfQuestions,
                onClick = { navigate(index) },
                onInfo = { /* Future Info logic */ },
                onEdit = { /* Future Edit logic */ },
                onDelete = { /* Future Delete logic */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseCard(
    name: String,
    questionCount: Int,
    onClick: () -> Unit,
    onInfo: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    OutlinedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .heightIn(min = 70.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = lightGray.copy(alpha = 0.1f),
            contentColor = whitish
        ),
        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = jetBrainsMonoFontFamily
                )
                Text(
                    text = "Questions: $questionCount",
                    fontSize = 14.sp,
                    color = lightGray,
                    fontFamily = jetBrainsMonoFontFamily
                )
            }

//            Dropdown menu
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = lightGray
                    )
                }

//              TODO: add functionalities of this lists
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    shape = RoundedCornerShape(20.dp),
                    containerColor = darkGray,
                    modifier = Modifier.width(140.dp)
                ) {
                    // INFO - about this test
                    DropdownMenuItem(
                        text = { Text("Info", fontSize = 16.sp) },
                        onClick = { onInfo(); menuExpanded = false },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    )

                    // EDIT
                    DropdownMenuItem(
                        text = { Text("Edit", fontSize = 16.sp) },
                        onClick = { onEdit(); menuExpanded = false },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    )

                    // REMOVE
                    DropdownMenuItem(
                        text = { Text("Remove", fontSize = 16.sp, color = Color.Red) },
                        onClick = { onDelete(); menuExpanded = false },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}