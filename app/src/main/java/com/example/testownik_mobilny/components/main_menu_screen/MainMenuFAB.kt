package com.example.testownik_mobilny.components.main_menu_screen

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.getFileNameFromUri
import com.example.testownik_mobilny.ui.theme.googleSansFlex
import com.example.testownik_mobilny.ui.theme.lighterDarkGray
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.unZip
import com.example.testownik_mobilny.view_models.MainActivityViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainMenuFAB(
    viewModel: MainActivityViewModel,
    isVisible: Boolean,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val fileExplorerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    LaunchedEffect(selectedFileUri) {
        selectedFileUri?.let { uri ->
            val name = getFileNameFromUri(context, uri).toString()
            val indexOfSep = name.indexOfLast { it == '.' }
            val newUri = "/${name.take(indexOfSep)}".toUri()

            unZip(context, uri, newUri.toString())
            viewModel.existingDatabases(context)
        }
    }

    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                modifier = Modifier.animateFloatingActionButton(
                    visible = isVisible || expanded,
                    alignment = Alignment.BottomEnd
                ),
                checked = expanded,
                onCheckedChange = { onExpandedChange(it) },
                containerSize = { 80.dp },
                containerCornerRadius = { 40.dp },
                containerColor = { lighterDarkGray }
            ) {
                val imageVector by remember {
                    derivedStateOf { if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add }
                }

                Icon(
                    painter = rememberVectorPainter(imageVector),
                    contentDescription = "placeholder", // TODO: placeholder
                    tint = whitish,
                    modifier = Modifier
                        .requiredSize(42.dp)
                        .animateIcon(
                            checkedProgress = { checkedProgress },
                            color = { whitish }
                        )
                )
            }
        }
    ) {
        // TODO: user will have option to create test from app
        FloatingActionButtonMenuItem(
            onClick = { onExpandedChange(false) },
            icon = { Icon(Icons.Filled.Create, contentDescription = null) },
            text = {
                Text(
                    text = "Create New",
                    textDecoration = TextDecoration.LineThrough,
                    fontFamily = googleSansFlex
                ) },
            contentColor = whitish,
            containerColor = lighterDarkGray
        )

        // TODO: user will have option to download test database from Github repository
        FloatingActionButtonMenuItem(
            onClick = { onExpandedChange(false) },
            icon = { Icon(painter = painterResource(R.drawable.github_mark_white), contentDescription = null) },
            text = {
                Text(
                    text = "From Github",
                    textDecoration = TextDecoration.LineThrough,
                    fontFamily = googleSansFlex
                ) },
            contentColor = whitish,
            containerColor = lighterDarkGray
        )

        FloatingActionButtonMenuItem(
            onClick = {
                onExpandedChange(false)
                fileExplorerLauncher.launch("*/*")
            },
            icon = {
                Icon(
                    Icons.Filled.FileDownload,
                    contentDescription = null,
                )
            },
            text = {
                Text(
                    text = "Import Local",
                    fontFamily = googleSansFlex
                ) },
            contentColor = whitish,
            containerColor = lighterDarkGray
        )
    }

    BackHandler(enabled = expanded) {
        onExpandedChange(false)
    }
}