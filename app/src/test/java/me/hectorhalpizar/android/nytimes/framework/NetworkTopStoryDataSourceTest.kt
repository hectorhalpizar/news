package me.hectorhalpizar.android.nytimes.framework

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.hectorhalpizar.android.nytimes.framework.network.NyTimesRestApi
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NetworkTopStoryDataSourceTest {

    private val restApi: NyTimesRestApi = mockk(relaxed = true)
    private val testing = NetworkTopStoryDataSource(restApi)

    @Test
    fun `getFromDevice request succeed`() = runBlocking {
        // Given
        coEvery {
            restApi.getTopStories(any(), any())
        } returns mockk(relaxed = true) {
            coEvery { isSuccessful } returns true
            coEvery { body()?.results } returns listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        }

        // When
        val result = testing.get(section = Section.ARTS)

        // Then
        assertEquals(3, result.size)
    }

    @Test
    fun `getFromDevice request succeed body null`() : Unit = runBlocking {
        // Given
        coEvery {
            restApi.getTopStories(any(), any())
        } returns mockk(relaxed = true) {
            coEvery { isSuccessful } returns true
            coEvery { body()?.results } returns null
        }

        try {
            // When
            testing.get(section = Section.ARTS)
        } catch (e: Exception) {
            // Then
            assertTrue(e is NetworkTopStoryDataSource.Error.Payload.BodyNull)
        }
    }

    @Test
    fun `getFromDevice request failed`() : Unit = runBlocking {
        // Given
        coEvery {
            restApi.getTopStories(any(), any())
        } returns mockk(relaxed = true) {
            coEvery { isSuccessful } returns false
            coEvery { errorBody() } returns mockk(relaxed = true)
        }

        try {
            // When
            testing.get(section = Section.ARTS)
        } catch (e: Exception) {
            // Then
            assertTrue(e is NetworkTopStoryDataSource.Error.ErrorBody)
        }
    }

    @Test
    fun `getFromDevice request failed error body null`() : Unit = runBlocking {
        // Given
        coEvery {
            restApi.getTopStories(any(), any())
        } returns mockk(relaxed = true) {
            coEvery { isSuccessful } returns false
            coEvery { errorBody() } returns null
        }

        try {
            // When
            testing.get(section = Section.ARTS)
        } catch (e: Exception) {
            // Then
            assertTrue(e is NetworkTopStoryDataSource.Error.Payload.ErrorBodyNull)
        }
    }

    @Test
    fun `store throws IllegalStateException`() : Unit = runBlocking {
        // Given
        val article: Article = mockk(relaxed = true)

        // When
        try {
            testing.store(article, Section.ARTS)
        } catch (e: Exception) {
            // Then
            assertTrue(e is IllegalStateException)
            assertEquals("There is not a Network function to Store an Top Story article.", e.message)
        }
    }
}