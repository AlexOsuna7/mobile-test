package com.example.mobile_test.features.scanner.presentation


sealed class ScannerUiState {
    object Idle: ScannerUiState()
    object Loading: ScannerUiState()
    object Success: ScannerUiState()
    class Error(val message:String): ScannerUiState()
}