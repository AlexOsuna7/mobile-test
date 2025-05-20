package com.example.mobile_test.core.cache

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class QrCacheManagerImplTest {

    private lateinit var context: Context
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var manager: QrCacheManagerImpl

    @Before
    fun setup() {
        context = mock()
        sharedPrefs = mock()
        editor = mock()

        whenever(context.getSharedPreferences(any(), any())).thenReturn(sharedPrefs)
        whenever(sharedPrefs.edit()).thenReturn(editor)
        whenever(editor.putString(any(), any())).thenReturn(editor)
        whenever(editor.putLong(any(), any())).thenReturn(editor)
        whenever(editor.remove(any())).thenReturn(editor)
        whenever(editor.clear()).thenReturn(editor)



        manager = QrCacheManagerImpl(context)
    }

    @Test
    fun `Given seed and expiration When saveSeed is called Then save to SharedPreferences`() {
        manager.saveSeed("abc", 123L)

        verify(editor).putString("seed", "abc")
        verify(editor).putLong("expires_at", 123L)
        verify(editor).apply()
    }

    @Test
    fun `Given expired seed When getCachedSeed is called Then return null`() {
        whenever(sharedPrefs.getString("seed", null)).thenReturn("abc")
        whenever(sharedPrefs.getLong("expires_at", -1)).thenReturn(System.currentTimeMillis() - 1000)

        val result = manager.getCachedSeed()

        assertNull(result)
    }
}