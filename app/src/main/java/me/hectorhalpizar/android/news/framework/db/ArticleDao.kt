package me.hectorhalpizar.android.news.framework.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface ArticleDao {
    @Insert(onConflict = REPLACE)
    suspend fun addArticle(article: ArticleEntity)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("DELETE FROM articles WHERE mainSection = :section")
    suspend fun deleteAllArticles(section: String)

    @Transaction
    @Query("SELECT * FROM articles WHERE mainSection = :section AND uri IN (SELECT DISTINCT(multimedia_uri) FROM multimedia)")
    suspend fun getTopStoriesWithMultimedia(section: String) : List<ArticleAndMultimediaEntity>
}