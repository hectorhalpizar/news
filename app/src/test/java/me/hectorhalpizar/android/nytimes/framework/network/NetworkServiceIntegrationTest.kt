package me.hectorhalpizar.android.nytimes.framework.network

import junit.framework.TestCase.*
import me.hectorhalpizar.android.nytimes.framework.NetworkTopStoryDataSource
import me.hectorhalpizar.core.nytimes.domain.Section
import org.junit.Test

class NetworkServiceIntegrationTest {
    @Test
    fun `NY Times successful request`() {
        val result = NetworkService.nyTimes.getTopStories(Section.ARTS.name.lowercase()).execute()
        assertTrue(result.isSuccessful)
        result.body()?.results?.let {
            assertTrue(it.isNotEmpty())
        }
    }
    @Test
    fun `NY Times unknown section request`() {
        val result = NetworkService.nyTimes.getTopStories("asdfg").execute()
        assertFalse(result.isSuccessful)
        println("Code = " + result.code())
        println("Error: ${result.errorBody()?.string()}")
    }

    @Test
    fun `NY Times incorrect API key request`() {
        val result = NetworkService.nyTimes.getTopStories(Section.ARTS.name.lowercase(), "asdfg").execute()
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