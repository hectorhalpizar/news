package me.hectorhalpizar.android.nytimes.framework.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import me.hectorhalpizar.core.nytimes.domain.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = REPLACE)
    fun addArticle(article: ArticleEntity)

    @Query("SELECT * FROM articles WHERE mainSection = :section")
    fun getTopStories(section: String) : List<ArticleEntity>

    @Query("SELECT * FROM articles")
    fun getTopStories() : List<ArticleEntity>
}