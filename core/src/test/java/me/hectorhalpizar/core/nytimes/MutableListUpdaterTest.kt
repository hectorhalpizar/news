package me.hectorhalpizar.core.nytimes

import org.junit.Assert.*
import org.junit.Test

class MutableListUpdaterTest {

    @Test
    fun `update empty list`() {
        val mutableList: MutableList<Int> = mutableListOf()
        val list: List<Int> = listOf(1, 2, 3, 4, 5)

        mutableList.update(list)

        list.forEachIndexed { index, value ->
            assertEquals(value, mutableList[index])
        }
    }

    @Test
    fun `update non-empty list`() {
        val mutableList: MutableList<Int> = mutableListOf(1, 2, 5)
        val inputList: List<Int> = listOf(1, 2, 3, 4, 5)
        val expectedList: List<Int> = listOf(1, 2, 5, 3, 4)

        mutableList.update(inputList)

        expectedList.forEachIndexed { index, value ->
            assertEquals(value, mutableList[index])
        }
    }
}