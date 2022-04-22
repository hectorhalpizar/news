package me.hectorhalpizar.core.news.data

import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.domain.Section

interface TopStoryDataSource {
    suspend fun store(article: Article, section: Section)
    suspend fun get(section: Section) : List<Article>
}