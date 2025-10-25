package com.example.testownik_mobilny.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Base for all circular buttons
 * @param icon source for [ImageVector] image displayed on button (from resource folder)
 * @param rotation rotation of icon [Image] from default position
 */
@Composable
fun RoundIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector  = Icons.Filled.AddCircle,
    iconDescription: String = "Icon",
    rotation: Float = 0f,
    enabled: Boolean = true
){
//    SETTINGS
    IconButton(
        onClick,
        modifier = Modifier.size(75.dp)
    ) {
        Icon(
            icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(34.dp).rotate(rotation)
        )
    }
}