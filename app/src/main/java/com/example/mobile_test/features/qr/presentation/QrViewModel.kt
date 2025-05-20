package com.example.mobile_test.features.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_test.features.qr.domain.usecase.GetSeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QrViewModel(
    private val getQrSeedUseCase: GetSeedUseCase
) : ViewModel() {

    private val _seedState = MutableStateFlow<SeedUiState>(SeedUiState.Idle)
    val seedState: StateFlow<SeedUiState> = _seedState.asStateFlow()


    fun fetchSeed() {
        viewModelScope.launch {
            _seedState.value = SeedUiState.Loading
            try {
                val seed = getQrSeedUseCase()
                _seedState.value = SeedUiState.Success(seed.first, seed.second, seed.third)
            } catch (e: Exception) {
                _seedState.value = SeedUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}