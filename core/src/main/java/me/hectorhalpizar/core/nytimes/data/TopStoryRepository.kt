package me.hectorhalpizar.core.nytimes.data

import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section

class TopStoryRepository {
    fun getRemoteTopStories(section: Section) : List<Article> {
        TODO("Not Implemented")
    }

    fun storeTopStoryOnDevice(article: Article) {
        TODO("Not Implemented")
    }

    fun getStoredTopStories(section: Section) : List<Article> {
        TODO("Not Implemented")
    }

    sealed class Error(cause: Throwable?): Exception(cause) {
        class Network(cause: Throwable?): Error(cause)
    }
}