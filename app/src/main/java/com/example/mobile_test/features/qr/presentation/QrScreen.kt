package com.example.mobile_test.features.qr.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mobile_test.R
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant

@Composable
fun QrScreen(
    viewModel: QrViewModel
) {
    val seedState by viewModel.seedState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = seedState) {
            is SeedUiState.Loading -> LoadingView()
            is SeedUiState.Success -> {
                val timeRemaining = rememberTimerUntil(state.expiresAt)
                QrContentView(
                    seed = state.seed,
                    qrBitmap = state.qrBitmap,
                    timeRemaining = timeRemaining.value,
                    onRefreshClick = viewModel::fetchSeed
                )
            }
            is SeedUiState.Error -> ErrorView(message = state.message, onRetry = viewModel::fetchSeed)
            is SeedUiState.Idle -> IdleView(onGenerate = viewModel::fetchSeed)
        }
    }
}

@Composable
fun QrContentView(
    seed: String,
    qrBitmap: Bitmap?,
    timeRemaining: Duration,
    onRefreshClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.qr_seed_label, seed),
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))

        qrBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.size(250.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (timeRemaining.isZero) {
            Text(
                text = stringResource(R.string.qr_seed_expired),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRefreshClick) {
                Text(text = stringResource(R.string.qr_generate_new))
            }
        } else {
            Text(
                text = stringResource(R.string.qr_expires_in, formatDuration(timeRemaining)),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun LoadingView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.qr_loading), style = MaterialTheme.typography.body1)
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.qr_error_prefix, message),
            color = MaterialTheme.colors.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.qr_retry_button))
        }
    }
}

@Composable
fun IdleView(onGenerate: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.qr_idle_message))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onGenerate) {
            Text(text = stringResource(R.string.qr_retry_button))
        }
    }
}

@Composable
fun rememberTimerUntil(expiresAt: Instant): State<Duration> {
    val remainingTime = remember {
        mutableStateOf(Duration.between(Instant.now(), expiresAt).coerceAtLeast(Duration.ZERO))
    }

    LaunchedEffect(expiresAt) {
        while (remainingTime.value > Duration.ZERO) {
            delay(1000L)
            val now = Instant.now()
            remainingTime.value = Duration.between(now, expiresAt).coerceAtLeast(Duration.ZERO)
        }
    }

    return remainingTime
}

@SuppressLint("DefaultLocale")
fun formatDuration(duration: Duration): String {
    val minutes = duration.toMinutes()
    val seconds = duration.seconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}