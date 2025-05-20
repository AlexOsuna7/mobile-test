package com.example.mobile_test.features.scanner.presentation

import com.example.mobile_test.MainCoroutineRule
import com.example.mobile_test.features.scanner.domain.repository.ScannerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import com.example.mobile_test.core.common.Result
import kotlinx.coroutines.flow.first


@ExperimentalCoroutinesApi
class ScannerViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val repository: ScannerRepository = mock()
    private lateinit var viewModel: ScannerViewModel

    @Before
    fun setup() {
        viewModel = ScannerViewModel(repository)
    }

    @Test
    fun `Given valid seed When validateSeed is called Then emit Success`() = runTest {
        whenever(repository.validateSeed("abc")).thenReturn(Result.success(Unit))

        viewModel.validateSeed("abc")

        val state = viewModel.state.first { it is ScannerUiState.Success }
        assertTrue(state is ScannerUiState.Success)
    }

    @Test
    fun `Given error result When validateSeed is called Then emit Error state`() = runTest {
        whenever(repository.validateSeed("abc")).thenReturn(Result.error("invalid"))

        viewModel.validateSeed("abc")

        val state = viewModel.state.first { it is ScannerUiState.Error }
        assertEquals("invalid", (state as ScannerUiState.Error).message)
    }
}