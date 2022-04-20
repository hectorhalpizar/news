package me.hectorhalpizar.android.nytimes.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import me.hectorhalpizar.android.nytimes.MainCoroutineRule
import me.hectorhalpizar.android.nytimes.getOrAwaitValue
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section
import me.hectorhalpizar.core.nytimes.usecase.FetchTopStoriesFlowUseCase
import me.hectorhalpizar.core.nytimes.usecase.Interactor
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TopStoriesViewModelTest {

    private val fetchTopStories: FetchTopStoriesFlowUseCase = mockk(relaxed = true)
    private val interactor = Interactor(fetchTopStories)
    private val testing: TopStoriesViewModel = TopStoriesViewModel(interactor)

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `fetch and feed`() = mainCoroutineRule.runBlockingTest {
        coEvery { fetchTopStories.invoke(any()) } returns getNewFeed()
        testing.fetch(Section.ARTS)
        assertTrue(testing.fetchingState.getOrAwaitValue() is TopStoriesViewModel.RequestState.Successful)
    }

    @Test
    fun `fetch with the use case error`() = mainCoroutineRule.runBlockingTest {
        coEvery { fetchTopStories.invoke(any()) } throws FetchTopStoriesFlowUseCase.Error.Caused(IllegalArgumentException("Unit Test"))

        try {
            testing.fetch(Section.ARTS)
        } catch(e: Exception) {
            assertTrue(e is FetchTopStoriesFlowUseCase.Error.Caused)
            assertTrue(testing.fetchingState.getOrAwaitValue() is TopStoriesViewModel.RequestState.Failed)
        }
    }

    private fun getNewFeed() : List<Article> =
        arrayListOf<Article>().apply {
            for (i in 0..20) {
                add(mockk(relaxed = true) {
                    every { uri } returns "https://$i"
                    every { title } returns "Mocked Title"
                })
            }
        }.toList()
}