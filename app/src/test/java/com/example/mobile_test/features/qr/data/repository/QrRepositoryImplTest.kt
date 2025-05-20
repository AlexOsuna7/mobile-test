package com.example.mobile_test.features.qr.data.repository

import com.example.mobile_test.MainCoroutineRule
import com.example.mobile_test.core.cache.CachedSeed
import com.example.mobile_test.core.cache.QrCacheManager
import com.example.mobile_test.features.qr.data.remote.QrApiService
import com.example.mobile_test.features.qr.domain.model.QrSeed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class QrRepositoryImplTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var repository: QrRepositoryImpl
    private val api: QrApiService = mock()
    private val cache: QrCacheManager = mock()

    @Before
    fun setup() {
        repository = QrRepositoryImpl(api, cache)
    }

    @Test
    fun `Given valid cached seed When getSeed is called Then return cached seed`(): Unit = runBlocking {
        // Given
        val cachedSeed = CachedSeed("cachedSeed", System.currentTimeMillis() + 10000)
        whenever(cache.getCachedSeed()).thenReturn(cachedSeed)

        // When
        val result = repository.getSeed()

        // Then
        assertEquals("cachedSeed", result.seed)
        verify(api, never()).getSeed()
    }

    @Test
    fun `Given no valid cached seed When getSeed is called Then fetch new seed from API`() = runBlocking {
        // Given
        whenever(cache.getCachedSeed()).thenReturn(null)
        val newSeed = QrSeed("newSeed", System.currentTimeMillis() + 10000)
        whenever(api.getSeed()).thenReturn(newSeed)

        // When
        val result = repository.getSeed()

        // Then
        assertEquals("newSeed", result.seed)
        verify(cache).saveSeed(newSeed.seed, newSeed.expiresAt)
    }
}
