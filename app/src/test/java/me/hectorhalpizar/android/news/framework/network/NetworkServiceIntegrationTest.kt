package me.hectorhalpizar.android.news.framework.network

import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import me.hectorhalpizar.core.news.domain.Section
import org.junit.Test

class NetworkServiceIntegrationTest {

    @Test
    fun `NY Times successful request`() : Unit = runBlocking {
        val result = NetworkService.newsApi.getTopStories(Section.ARTS.sectionName.lowercase())
        assertTrue(result.isSuccessful)
        result.body()?.results?.let {
            assertTrue(it.isNotEmpty())
        }
    }

    @Test
    fun `NY Times unknown section request`() = runBlocking {
        val result = NetworkService.newsApi.getTopStories("asdfg")
        assertFalse(result.isSuccessful)
    }

    @Test
    fun `NY Times incorrect API key request`(): Unit = runBlocking {
        val result = NetworkService.newsApi.getTopStories(Section.ARTS.sectionName.lowercase(), "asdfg")
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