package me.hectorhalpizar.core.nytimes.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.hectorhalpizar.core.nytimes.data.TopStoryRepository
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FetchTopStoriesUseCaseTest {
    private val repository: TopStoryRepository = mockk(relaxed = true)
    private val test = FetchTopStoriesUseCase(repository)

    @Test
    fun `Results from the server returned`() {
        // Given
        every { repository.getRemoteTopStories(any()) } returns listOf(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )

        every { repository.storeTopStoryOnDevice(any(), any()) } returns Unit

        // When
        val result = test(Section.ARTS)

        // Then
        verify(exactly = 3) { repository.storeTopStoryOnDevice(any(), any()) }
        verify(exactly = 0) { repository.getStoredTopStories(any()) }
        assertEquals(3, result.size)
    }

    @Test
    fun `Server with empty articles list`() {
        // Given
        every { repository.getRemoteTopStories(any()) } returns listOf()
        every { repository.getStoredTopStories(any()).size } returns 3

        // When
        test(Section.ARTS)

        // Then
        verify(exactly = 0) { repository.storeTopStoryOnDevice(any(), any()) }
        verify(exactly = 1) { repository.getStoredTopStories(any()) }
    }

    @Test
    fun `Results from the server returned but not all the articles stored in the device`() {
        // Given
        val articleWithExceptionToBeThrown : Article = mockk(relaxed = true)
        every { repository.getRemoteTopStories(any()) } returns listOf(
            mockk(relaxed = true),
            articleWithExceptionToBeThrown,
            mockk(relaxed = true)
        )

        every { repository.storeTopStoryOnDevice(articleWithExceptionToBeThrown, any()) } throws Exception("Unit Test Exception")

        // When
        val result = test(Section.ARTS)

        // Then
        verify(exactly = 2) { repository.storeTopStoryOnDevice(any(), any()) }
        verify(exactly = 0) { repository.getStoredTopStories(any()) }
        assertEquals(3, result.size)
    }

    @Test
    fun `Network error happened fetching server articles`() {
        // Given
        every { repository.getRemoteTopStories(any()) } throws TopStoryRepository.Error.Network(null)

        // When
        test(Section.ARTS)

        // Then
        verify(exactly = 0) { repository.storeTopStoryOnDevice(any(), any()) }
        verify(exactly = 1) { repository.getStoredTopStories(any()) }
    }

    @Test
    fun `Network and local error happened fetching articles`() {
        // Given
        every { repository.getRemoteTopStories(any()) } throws TopStoryRepository.Error.Network(null)
        every { repository.getStoredTopStories(any()) } throws IllegalStateException("Unit Test Exception")


        // When
        val exception = assertThrows(Exception::class.java) {
            test(Section.ARTS)
        }

        // Then
        assertTrue(exception is FetchTopStoriesUseCase.Error.OnLocalRepository)
        verify(exactly = 0) { repository.storeTopStoryOnDevice(any(), any()) }
        verify(exactly = 1) { repository.getStoredTopStories(any()) }
    }
}