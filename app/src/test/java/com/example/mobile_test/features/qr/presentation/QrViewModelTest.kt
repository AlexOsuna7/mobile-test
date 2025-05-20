package com.example.mobile_test.features.qr.presentation

import android.graphics.Bitmap
import com.example.mobile_test.MainCoroutineRule
import com.example.mobile_test.features.qr.domain.usecase.GetSeedUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.time.Instant

@ExperimentalCoroutinesApi
class QrViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val useCase: GetSeedUseCase = mock()
    private lateinit var viewModel: QrViewModel
    private val mockBitmap: Bitmap = mock()


    @Before
    fun setup() {
        viewModel = QrViewModel(useCase)
    }

    @Test
    fun `Given success from use case When fetchSeed is called Then emit Success state`() = runTest {
        val instant = Instant.now()
        val seed = Triple("abc", mockBitmap, instant)
        whenever(useCase()).thenReturn(seed)

        viewModel.fetchSeed()

        val state = viewModel.seedState.first { it is SeedUiState.Success }
        assertTrue(state is SeedUiState.Success)
        assertEquals("abc", (state as SeedUiState.Success).seed)
    }


    @Test
    fun `Given exception from use case When fetchSeed is called Then emit Error state`() = runTest {
        whenever(useCase()).thenThrow(RuntimeException("error"))

        viewModel.fetchSeed()

        val state = viewModel.seedState.first { it is SeedUiState.Error }
        assertTrue(state is SeedUiState.Error)
        assertEquals("error", (state as SeedUiState.Error).message)
    }
}