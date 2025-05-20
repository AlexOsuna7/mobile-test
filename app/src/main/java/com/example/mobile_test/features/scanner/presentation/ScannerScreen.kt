package com.example.mobile_test.features.scanner.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is ScannerUiState.Idle -> {
            ZXingScannerView(
                modifier = Modifier.fillMaxSize(),
                onQRCodeScanned = { scannedCode ->
                    viewModel.validateSeed(scannedCode)
                }
            )
        }

        is ScannerUiState.Loading -> {
            LoadingContent()
        }

        is ScannerUiState.Success -> {
            SuccessContent()
        }

        is ScannerUiState.Error -> {
            val errorMessage = (state as ScannerUiState.Error).message
            ErrorContent(
                message = errorMessage,
                onRetry = { newSeed -> viewModel.validateSeed(newSeed) },
                onRescan = { viewModel.resetState() }
            )
        }
    }
}

@Composable
fun IdleContent(onValidate: (String) -> Unit) {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Enter seed to validate", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Seed") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (input.isNotBlank()) {
                    onValidate(input.trim())
                }
            },
            enabled = input.isNotBlank(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Validate")
        }
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SuccessContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Seed is valid!",
            color = Color.Green,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: (String) -> Unit,
    onRescan: () -> Unit
) {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $message",
            color = Color.Red,
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Try Manually",
            color = Color.Red,
            style = MaterialTheme.typography.body1
        )

        TextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Seed...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onRetry(input) }
        ) {
            Text("Retry")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onRescan
        ) {
            Text("Scan again")
        }
    }
}