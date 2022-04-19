package me.hectorhalpizar.android.nytimes.framework.network

import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import me.hectorhalpizar.android.nytimes.framework.NetworkTopStoryDataSource
import me.hectorhalpizar.core.nytimes.domain.Section
import org.junit.Test

class NetworkServiceIntegrationTest {

    @Test
    fun `NY Times successful request`() : Unit = runBlocking {
        val result = NetworkService.nyTimes.getTopStories(Section.ARTS.name.lowercase())
        assertTrue(result.isSuccessful)
        result.body()?.results?.let {
            assertTrue(it.isNotEmpty())
        }
    }

    @Test
    fun `NY Times unknown section request`() = runBlocking {
        val result = NetworkService.nyTimes.getTopStories("asdfg")
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `NY Times incorrect API key request`(): Unit = runBlocking {
        val result = NetworkService.nyTimes.getTopStories(Section.ARTS.name.lowercase(), "asdfg")
        assertFalse(result.isSuccessful)
        println("Code = " + result.code())
        val `is` = result.errorBody()?.byteStream()
        `is`?.let {
            it.reader().readLines().forEach { line ->
                println(line)
            }
        }
    }
}