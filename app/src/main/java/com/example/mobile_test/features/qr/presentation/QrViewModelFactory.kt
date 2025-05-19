package com.example.mobile_test.features.qr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_test.features.qr.domain.usecase.GetSeedUseCase
import javax.inject.Inject

class QrViewModelFactory @Inject constructor(
    private val getSeedUseCase: GetSeedUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QrViewModel::class.java)){
            return QrViewModel(getSeedUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}