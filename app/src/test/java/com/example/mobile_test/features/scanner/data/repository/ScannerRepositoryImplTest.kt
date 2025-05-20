package com.example.mobile_test.features.scanner.data.repository

import com.example.mobile_test.features.scanner.data.remote.QrValidationApi
import com.example.mobile_test.features.scanner.domain.model.ValidationResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import com.example.mobile_test.core.common.Result


@ExperimentalCoroutinesApi
class ScannerRepositoryImplTest {

    private val api: QrValidationApi = mock()
    private lateinit var repository: ScannerRepositoryImpl

    @Before
    fun setup() {
        repository = ScannerRepositoryImpl(api)
    }

    @Test
    fun `Given valid response When validateSeed is called Then return success`() = runBlocking {
        val response = Response.success(ValidationResponse(true))
        whenever(api.validateSeed("abc")).thenReturn(response)

        val result = repository.validateSeed("abc")

        assertTrue(result is Result.Success)
    }

    @Test
    fun `Given invalid seed When validateSeed is called Then return error`() = runBlocking {
        val response = Response.success(ValidationResponse(false, "Invalid"))
        whenever(api.validateSeed("abc")).thenReturn(response)

        val result = repository.validateSeed("abc")

        assertTrue(result is Result.Error)
        assertEquals("Invalid", (result as Result.Error).message)
    }
}