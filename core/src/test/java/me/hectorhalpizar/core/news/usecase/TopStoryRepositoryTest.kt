package me.hectorhalpizar.core.news.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.hectorhalpizar.core.news.data.TopStoryDataSource
import me.hectorhalpizar.core.news.data.TopStoryRepository
import me.hectorhalpizar.core.news.domain.Section
import org.junit.Assert.*
import org.junit.Test

class TopStoryRepositoryTest {

    private val localDataSource: TopStoryDataSource = mockk(relaxed = true)
    private val remoteDataSource: TopStoryDataSource = mockk(relaxed = true)
    private val testing = TopStoryRepository(localDataSource, remoteDataSource)

    @Test
    fun `getRemoteTopStories successfully`() = runBlocking {
        // Given
        coEvery { remoteDataSource.get(any()) } returns listOf(mockk(relaxed = true))

        // When
        val result = testing.getRemoteTopStories(Section.ARTS)

        // Then
        assertEquals(1, result.size)
    }

    @Test
    fun `getRemoteTopStories failed`() : Unit = runBlocking {
        // Given
        coEvery { remoteDataSource.get(any()) } throws IllegalStateException("Unit Test Exception")

        try {
            // When
            testing.getRemoteTopStories(Section.ARTS)
        } catch (e: Exception) {
            // Then
            assertTrue(e is TopStoryRepository.Error.Network)
        }
    }
}