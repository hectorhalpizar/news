package me.hectorhalpizar.core.nytimes.data

import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section

interface TopStoryDataSource {
    suspend fun store(article: Article, section: Section)
    suspend fun get(section: Section) : List<Article>
}