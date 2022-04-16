package me.hectorhalpizar.core.nytimes.data

import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section

interface TopStoryDataSource {
    fun store(article: Article, section: Section)
    fun getFromDevice(section: Section) : List<Article>
}