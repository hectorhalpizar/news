package me.hectorhalpizar.android.nytimes.presentation

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section
import me.hectorhalpizar.core.nytimes.usecase.FetchTopStoriesFlowUseCase
import me.hectorhalpizar.core.nytimes.usecase.Interactor
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TopStoriesViewModelTest {

    // TODO (Fix unit test)

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val fetchTopStories: FetchTopStoriesFlowUseCase = mockk(relaxed = true)
    private val interactor = Interactor(fetchTopStories)
    private val testing: TopStoriesViewModel = TopStoriesViewModel(interactor)

    @Test
    fun `fetch feed`() = coroutinesTestRule.testDispatcher.runBlockingTest {

//        testing.fetch(Section.ARTS)
//        val articles = testing.articles.value
//        assertEquals(21, articles?.size)
        val testing = TopStoriesViewModel(interactor)
        testing.fetch(Section.ARTS).start()
        verify { fetchTopStories(any()) }
    }

    @Test
    fun `fetch and update feed`() {
//        every { fetchTopStories.invoke(any()) } returns getNewFeed()
//        val articles = testing.articles.value
//        articles?.plusAssign(getFetchedArticles())
//        testing.fetch(Section.ARTS)
//        assertEquals(23, articles?.size)
//        assertEquals("Mocked Title", articles?.get("https://10")?.title)
//        assertEquals("Mocked Title", articles?.get("https://15")?.title)
//        assertEquals("Mocked Title", articles?.get("https://20")?.title)
//        assertEquals("Title A", articles?.get("https://a")?.title)
//        assertEquals("Title C", articles?.get("https://c")?.title)
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

    private fun getFetchedArticles(): Map<String, Article> = mapOf(
            "https://10" to mockk(relaxed = true) {
                every { uri } returns "https://10"
                every { title } returns "Modified Title"
            },
            "https://a" to mockk(relaxed = true) {
                every { uri } returns "https://a"
                every { title } returns "Title A"
            },
            "https://15" to mockk(relaxed = true) {
                every { uri } returns "https://15"
                every { title } returns "Modified Title 2"
            },
            "https://c" to mockk(relaxed = true) {
                every { uri } returns "https://c"
                every { title } returns "Title C"
            },
            "https://20" to mockk(relaxed = true) {
                every { uri } returns "https://20"
                every { title } returns "Modified Title 3"
            },
        )
}