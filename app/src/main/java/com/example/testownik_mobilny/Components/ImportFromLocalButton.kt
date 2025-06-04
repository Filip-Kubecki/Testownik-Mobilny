package com.example.testownik_mobilny.Components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.testownik_mobilny.R
import com.example.testownik_mobilny.getFileNameFromUri
import com.example.testownik_mobilny.ui.theme.lighterGray
import com.example.testownik_mobilny.unZip
import java.io.File

@Composable
fun ImportFromLocalButton(
    modifier: Modifier = Modifier,
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
    Button(
        onClick = {
            fileExplorerLauncher.launch("*/*")
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = lighterGray
        ),
        modifier = modifier
            .size(75.dp, 75.dp)
    ){
        Image(
            painter = painterResource(R.drawable.import_data_icon),
            contentDescription = "Import data from local",
            contentScale = ContentScale.Fit
        )
    }

//    Function layer
    selectedFileUri?.let { uri ->
        val name = getFileNameFromUri(context, uri).toString()
        val indexOfSep: Int = if (name.contains('.')) {
            name.indexOf('.')
        } else {
            -1
        }
        val newUri = "/${name.dropLast(name.length-indexOfSep)}".toUri()

        unZip(context, uri, newUri.toString())

        val extractedFiles = File(context.filesDir, newUri.toString()).listFiles()

//        if (extractedFiles != null && extractedFiles[0].isFile) {
//            Text(
//                "FILE CONTENT:\n ${extractedFiles[0].readText()}",
//                color = whitish,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
    }
}