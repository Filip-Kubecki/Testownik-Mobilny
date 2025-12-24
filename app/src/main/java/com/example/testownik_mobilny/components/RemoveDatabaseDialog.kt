package com.example.testownik_mobilny.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.ui.theme.googleSansFlex
import com.example.testownik_mobilny.ui.theme.removeRed
import com.example.testownik_mobilny.view_models.MainActivityViewModel

@Composable
fun RemoveDatabaseDialog(
    database: QuestionDatabase,
    viewmodel: MainActivityViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.errorContainer
            ) },
        title = {
            Text(text = "Delete Database", fontFamily = googleSansFlex)
        },
        text = {
            Text(
                text = stringResource(R.string.RemoveDatabaseDialogMainText, database.name),
                fontFamily = googleSansFlex
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewmodel.removeDataBase(database)
                    onDismiss()
                }
            ) {
                Text(
                    "Delete",
                    color = removeRed,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}