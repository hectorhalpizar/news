package me.hectorhalpizar.core.nytimes.usecase

import io.mockk.every
import io.mockk.mockk
import me.hectorhalpizar.core.nytimes.data.TopStoryDataSource
import me.hectorhalpizar.core.nytimes.data.TopStoryRepository
import me.hectorhalpizar.core.nytimes.domain.Section
import org.junit.Assert.*
import org.junit.Test

class TopStoryRepositoryTest {

    private val localDataSource: TopStoryDataSource = mockk(relaxed = true)
    private val remoteDataSource: TopStoryDataSource = mockk(relaxed = true)
    private val testing = TopStoryRepository(localDataSource, remoteDataSource)

    @Test
    fun `getRemoteTopStories successfully`() {
        // Given
        every { remoteDataSource.get(any()) } returns listOf(mockk(relaxed = true))

        // When
        val result = testing.getRemoteTopStories(Section.ARTS)

        // Then
        assertEquals(1, result.size)
    }

    @Test
    fun `getRemoteTopStories failed`() {
        // Given
        every { remoteDataSource.get(any()) } throws IllegalStateException("Unit Test Exception")

        // When
        val exception = assertThrows(Exception::class.java) {
            testing.getRemoteTopStories(Section.ARTS)
        }

        // Then
        assertTrue(exception is TopStoryRepository.Error.Network)
    }
}