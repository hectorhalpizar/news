package me.hectorhalpizar.android.nytimes.framework

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import me.hectorhalpizar.android.nytimes.framework.db.NyTimesRoomDatabase
import me.hectorhalpizar.android.nytimes.framework.network.Result
import me.hectorhalpizar.core.nytimes.domain.Section
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomTopStoryDataSourceTest {
    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var database: NyTimesRoomDatabase
    private lateinit var test: RoomTopStoryDataSource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(context, NyTimesRoomDatabase::class.java).build()
        test = RoomTopStoryDataSource(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun store_articles_to_database_avoiding_repeated_ones() {
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
        assertThat("The feed.json articles amount does not match the SQLite storing", feed.results.size == test.getFromDevice(Section.ARTS).size)
    }
}