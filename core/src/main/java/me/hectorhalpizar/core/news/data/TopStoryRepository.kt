package me.hectorhalpizar.core.news.data

import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.domain.Section
class TopStoryRepository(
    private val localDataSource: TopStoryDataSource,
    private val remoteDataSource: TopStoryDataSource
) {
    suspend fun getRemoteTopStories(section: Section) : List<Article> =
        try {
            remoteDataSource.get(section)
        } catch (e: Exception) {
            throw Error.Network(e)
        }

    suspend fun storeTopStoriesOnDevice(article: List<Article>, section: Section) {
        localDataSource.deleteAllArticles(section)
        article.forEach {
            localDataSource.store(it, section)
        }
    }

    suspend fun getStoredTopStories(section: Section) : List<Article> = localDataSource.get(section)

    sealed class Error(cause: Throwable?): Exception(cause) {
        class Network(cause: Throwable?): Error(cause)
    }
}