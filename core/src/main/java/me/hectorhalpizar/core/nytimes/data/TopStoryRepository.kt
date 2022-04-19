package me.hectorhalpizar.core.nytimes.data

import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section

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

    suspend fun storeTopStoryOnDevice(article: Article, section: Section) = localDataSource.store(article, section)

    suspend fun getStoredTopStories(section: Section) : List<Article> = localDataSource.get(section)

    sealed class Error(cause: Throwable?): Exception(cause) {
        class Network(cause: Throwable?): Error(cause)
    }
}