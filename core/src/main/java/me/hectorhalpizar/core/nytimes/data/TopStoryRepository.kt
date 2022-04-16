package me.hectorhalpizar.core.nytimes.data

import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section

class TopStoryRepository(
    private val dataSource: TopStoryDataSource
) {
    fun getRemoteTopStories(section: Section) : List<Article> {
        TODO("Not Implemented")
    }

    fun storeTopStoryOnDevice(article: Article, section: Section) = dataSource.store(article, section)

    fun getStoredTopStories(section: Section) : List<Article> = dataSource.getFromDevice(section)

    sealed class Error(cause: Throwable?): Exception(cause) {
        class Network(cause: Throwable?): Error(cause)
    }
}