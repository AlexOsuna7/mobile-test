package com.example.mobile_test.features.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mobile_test.R

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.home_welcome_title),
                style = MaterialTheme.typography.h5,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.home_welcome_message),
                style = MaterialTheme.typography.body1,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
fun FabMenu(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onQrClick: () -> Unit,
    onScanClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
    ) {
        AnimatedVisibility(visible = isExpanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FabMenuItem(
                    icon = Icons.Default.QrCode2,
                    contentDescription = stringResource(R.string.fab_qr_content_description),
                    onClick = onQrClick
                )

                FabMenuItem(
                    icon = Icons.Default.CameraAlt,
                    contentDescription = stringResource(R.string.fab_scan_content_description),
                    onClick = onScanClick
                )
            }
        }

        FloatingActionButton(
            onClick = onToggle,
            backgroundColor = Color.Black,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Menu,
                contentDescription = stringResource(R.string.fab_toggle_menu_content_description)
            )
        }
    }
}

@Composable
fun FabMenuItem(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = Color.LightGray,
        contentColor = Color.Black,
        modifier = Modifier.size(56.dp)
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}