package me.hectorhalpizar.core.news.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import me.hectorhalpizar.core.news.data.TopStoryRepository
import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.domain.Section
import org.junit.Assert.assertEquals
import org.junit.Test

class FetchTopStoriesUseCaseTest  {
    private val repository: TopStoryRepository = mockk(relaxed = true)
    private val testing = FetchTopStoriesUseCase(repository)

    @Test
    fun `Results from the server returned`() = runBlocking {
        // Given
        coEvery { repository.getRemoteTopStories(any()) } returns listOf(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )

        coEvery { repository.storeTopStoriesOnDevice(any(), any()) } returns Unit

        // When
        val result = testing(Section.ARTS)

        // Then
        coVerify(exactly = 1) { repository.storeTopStoriesOnDevice(any(), any()) }
        coVerify(exactly = 0) { repository.getStoredTopStories(any()) }
        assertEquals(3, result.size)
    }

    @Test
    fun `Server with empty articles list`() = runBlocking {
        // Given
        coEvery { repository.getRemoteTopStories(any()) } returns listOf()
        coEvery { repository.getStoredTopStories(any()).size } returns 3

        // When
        testing(Section.ARTS)

        // Then
        coVerify(exactly = 0) { repository.storeTopStoriesOnDevice(any(), any()) }
        coVerify(exactly = 1) { repository.getStoredTopStories(any()) }
    }

    @Test
    fun `Results from the server returned but not all the articles stored in the device`() = runBlocking {
        // Given
        val articleWithExceptionToBeThrown : Article = mockk(relaxed = true)
        coEvery { repository.getRemoteTopStories(any()) } returns listOf(
            mockk(relaxed = true),
            articleWithExceptionToBeThrown,
            mockk(relaxed = true)
        )

        coEvery { repository.storeTopStoriesOnDevice(any(), any()) } throws Exception("Unit Test Exception")

        // When
        val result = testing(Section.ARTS)

        // Then
        coVerify(exactly = 1) { repository.storeTopStoriesOnDevice(any(), any()) }
        coVerify(exactly = 0) { repository.getStoredTopStories(any()) }
        assertEquals(3, result.size)
    }

    @Test
    fun `Network and local error happened fetching articles`(): Unit = runBlocking {
        // Given
        coEvery { repository.getRemoteTopStories(any()) } throws TopStoryRepository.Error.Network(null)
        coEvery { repository.getStoredTopStories(any()) } throws IllegalStateException("Unit Test Exception")


        // When
        try {
            testing(Section.ARTS)
        } catch (e: Exception) {
        // Then
            assertTrue(e is FetchTopStoriesUseCase.Error.Caused)
            coVerify(exactly = 0) { repository.storeTopStoriesOnDevice(any(), any()) }
            coVerify(exactly = 1) { repository.getStoredTopStories(any()) }
        }
    }
}
