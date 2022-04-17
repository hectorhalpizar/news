package me.hectorhalpizar.android.nytimes.framework.extensions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class MutableLiveDataExtensionsTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Add plus item list extension`() {
        val list1 : MutableLiveData<List<Int>> = MutableLiveData<List<Int>>(listOf(1, 2, 3, 4, 5))
        assertEquals(list1.value?.size, 5)
        list1 += listOf(6, 7, 8, 9)
        assertEquals(list1.value?.size, 9)
    }
}