package com.example.mobile_test.features.qr.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mobile_test.R

@Composable
fun QrScreen(
    viewModel: QrViewModel
) {
    val seedState by viewModel.seedState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (seedState) {
            is SeedUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.qr_loading), style = MaterialTheme.typography.body1)
            }
            is SeedUiState.Success -> {
                val successState = seedState as SeedUiState.Success
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = stringResource(R.string.qr_seed_label, successState.seed))
                    Spacer(modifier = Modifier.height(16.dp))
                    successState.qrBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(250.dp)
                        )
                    }
                }
            }
            is SeedUiState.Error -> {
                val error = (seedState as SeedUiState.Error).message
                Text(
                    text = stringResource(R.string.qr_error_prefix, error),
                    color = MaterialTheme.colors.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.fetchSeed() }) {
                    Text(text = stringResource(R.string.qr_retry_button))
                }
            }
            is SeedUiState.Idle -> {
                Text(text = stringResource(R.string.qr_idle_message))
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.fetchSeed() }) {
                    Text(text = stringResource(R.string.qr_retry_button))
                }
            }
        }
    }
}