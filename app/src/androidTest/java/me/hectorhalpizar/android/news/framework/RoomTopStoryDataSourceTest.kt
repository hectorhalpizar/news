package me.hectorhalpizar.android.news.framework

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import me.hectorhalpizar.android.news.framework.db.ArticleEntity
import me.hectorhalpizar.android.news.framework.db.NewsRoomDatabase
import me.hectorhalpizar.android.news.framework.network.Result
import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.domain.Section
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomTopStoryDataSourceTest {
    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var database: NewsRoomDatabase
    private lateinit var test: RoomTopStoryDataSource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(context, NewsRoomDatabase::class.java).build()
        test = RoomTopStoryDataSource(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun store_articles_to_database_avoiding_repeated_ones() = runBlocking  {
        // Given
        val feedReader = context.assets.open("feed.json").reader()
        val feed = Gson().fromJson(feedReader, Result::class.java)

        // When
        feed.results.forEach {
            test.store(it, Section.ARTS)
            test.store(it, Section.ARTS)
            test.store(it, Section.ARTS)
        }

        // Then
        val resultDb = test.get(Section.ARTS)

        val articlesFeedSize = feed.results.size
        val articlesDbSize = resultDb.size
        val reasonFailArticles = "Articles: Feed = ${articlesFeedSize}, Database = $articlesDbSize}"
        assertThat(reasonFailArticles, articlesFeedSize == articlesDbSize)

        val articleFromFirstMultimediaFeedSize = feed.results.first().multimedia.size
        val articleFromFirstMultimediaDbSize = resultDb.first().multimedia.size
        val reasonFailMultimedia = "Multimedia in first article: Feed = ${articleFromFirstMultimediaFeedSize}, Database = $articleFromFirstMultimediaDbSize}"
        assertThat(reasonFailMultimedia, articleFromFirstMultimediaFeedSize == articleFromFirstMultimediaDbSize)
    }

    @Test
    fun delete_first_article() = runBlocking  {
        // Given
        val feedReader = context.assets.open("feed.json").reader()
        val feed = Gson().fromJson(feedReader, Result::class.java)

        feed.results.forEach {
            test.store(it, Section.ARTS)
        }

        val totalArticles = feed.results.size
        val articleToDelete = feed.results.first()
        val totalMultimedia = getTotalMultimedia(feed.results)

        // When
        test.delete(articleToDelete, Section.ARTS)

        // Then
        val result = test.get(Section.ARTS)
        val resultFirstArticle = result.first()
        val totalArticlesAfterDeletion = result.size
        val totalMultimediaFromResult = getTotalMultimedia(result)

        val reasonOneDeletionFailed = "After deletion you have the same or maybe more articles than before"
        assertThat(reasonOneDeletionFailed, totalArticles > totalArticlesAfterDeletion)

        val reasonTwoDeletionFailed = "You have the same first article before the deletion"
        assertThat(reasonTwoDeletionFailed, resultFirstArticle != articleToDelete)

        val reasonThreeDeletionFailed = "You have the same or maybe more amount of multimedia in the articles list before deletion"
        assertThat(reasonThreeDeletionFailed, totalMultimedia != totalMultimediaFromResult)
    }

    private fun getTotalMultimedia(articles: List<Article>) : Int {
        var total = 0
        articles.forEach { total += it.multimedia.size }
        return total
    }
}