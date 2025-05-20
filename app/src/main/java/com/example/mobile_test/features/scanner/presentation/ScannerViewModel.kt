package com.example.mobile_test.features.scanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_test.features.scanner.domain.repository.ScannerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.mobile_test.core.common.Result


class ScannerViewModel(
    private val repository: ScannerRepository
): ViewModel() {

    private val _state = MutableStateFlow<ScannerUiState>(ScannerUiState.Idle)
    val state: StateFlow<ScannerUiState> = _state

    fun validateSeed(seed: String) {
        viewModelScope.launch {
            _state.value = ScannerUiState.Loading
            when (val result = repository.validateSeed(seed)) {
                is Result.Success -> _state.value = ScannerUiState.Success
                is Result.Error -> _state.value = ScannerUiState.Error(result.message)
            }
        }
    }

    fun resetState() {
        _state.value = ScannerUiState.Idle
    }
}