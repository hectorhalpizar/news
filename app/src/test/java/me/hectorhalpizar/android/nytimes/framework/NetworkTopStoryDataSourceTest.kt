package me.hectorhalpizar.android.nytimes.framework

import io.mockk.every
import io.mockk.mockk
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
    private val test = NetworkTopStoryDataSource(restApi)

    @Test
    fun `getFromDevice request succeed`() {
        // Given
        every {
            restApi.getTopStories(any(), any()).execute()
        } returns mockk(relaxed = true) {
            every { isSuccessful } returns true
            every { body()?.results } returns listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        }

        // When
        val result = test.getFromDevice(section = Section.ARTS)

        // Then
        assertEquals(3, result.size)
    }

    @Test
    fun `getFromDevice request succeed body null`() {
        // Given
        every {
            restApi.getTopStories(any(), any()).execute()
        } returns mockk(relaxed = true) {
            every { isSuccessful } returns true
            every { body()?.results } returns null
        }

        // When
        val exception = assertThrows(Exception::class.java) {
            test.getFromDevice(section = Section.ARTS)
        }

        // Then
        assertTrue(exception is NetworkTopStoryDataSource.Error.Payload.BodyNull)
    }

    @Test
    fun `getFromDevice request failed`() {
        // Given
        every {
            restApi.getTopStories(any(), any()).execute()
        } returns mockk(relaxed = true) {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk(relaxed = true)
        }

        // When
        val exception = assertThrows(Exception::class.java) {
            test.getFromDevice(section = Section.ARTS)
        }

        // Then
        assertTrue(exception is NetworkTopStoryDataSource.Error.ErrorBody)
    }

    @Test
    fun `getFromDevice request failed error body null`() {
        // Given
        every {
            restApi.getTopStories(any(), any()).execute()
        } returns mockk(relaxed = true) {
            every { isSuccessful } returns false
            every { errorBody() } returns null
        }

        // When
        val exception = assertThrows(Exception::class.java) {
            test.getFromDevice(section = Section.ARTS)
        }

        // Then
        assertTrue(exception is NetworkTopStoryDataSource.Error.Payload.ErrorBodyNull)
    }

    @Test
    fun `store throws IllegalStateException`() {
        // Given
        val article: Article = mockk(relaxed = true)

        // When
        val exception = assertThrows(Exception::class.java) {
            test.store(article, Section.ARTS)
        }

        // Then
        assertTrue(exception is IllegalStateException)
        assertEquals("There is not a Network function to Store an Top Story article.", exception.message)
    }
}