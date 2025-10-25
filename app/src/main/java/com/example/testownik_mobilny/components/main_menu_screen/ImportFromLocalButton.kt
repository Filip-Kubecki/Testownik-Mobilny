package com.example.testownik_mobilny.components.main_menu_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.testownik_mobilny.getFileNameFromUri
import com.example.testownik_mobilny.ui.theme.darkGray
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.ui.theme.whitish
import com.example.testownik_mobilny.unZip
import com.example.testownik_mobilny.view_models.MainActivityViewModel

/**
 * Button that opens file explorer and lets you choose database .zip file
 * that is imported as new database
 */
@Composable
fun ImportFromLocalButton(
    modifier: Modifier = Modifier,
    viewModel: MainActivityViewModel
){
//    Parameters
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val fileExplorerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

//    UI layer
    IconButton(
        onClick = {
            fileExplorerLauncher.launch("*/*")
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = lighterGray,
            contentColor = whitish,
            disabledContentColor = darkGray,
            disabledContainerColor = darkGray
        ),
        modifier = modifier
            .size(75.dp)
    ){
        Icon(
            Icons.Filled.ExitToApp,
            "",
            modifier = Modifier
                .rotate(90f)
                .size(34.dp)
        )
    }

//    Function layer
    LaunchedEffect(selectedFileUri) {
        selectedFileUri?.let { uri ->
            val name = getFileNameFromUri(context, uri).toString()
            val indexOfSep = name.indexOfLast { it == '.' }
            val newUri = "/${name.take(indexOfSep)}".toUri()

            // Unzip and update database list
            unZip(context, uri, newUri.toString())
            viewModel.existingDatabases(context)

            // Reset URI to avoid repeated calls on recomposition
            selectedFileUri = null
        }
    }
}