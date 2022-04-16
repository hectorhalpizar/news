package me.hectorhalpizar.android.nytimes.framework

import android.content.Context
import me.hectorhalpizar.android.nytimes.framework.db.*
import me.hectorhalpizar.core.nytimes.data.TopStoryDataSource
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section
import java.util.*

class RoomTopStoryDataSource internal constructor(database: NyTimesRoomDatabase): TopStoryDataSource {

    constructor(context: Context) : this(NyTimesRoomDatabase.getInstance(context))

    private val articleDao: ArticleDao = database.articleDao()

    override fun store(article: Article, section: Section) {
        article.let { a ->
            articleDao.addArticle(map(a, mainSection = section.name))
        }
    }

    override fun getFromDevice(section: Section): List<Article> =
        articleDao.getTopStories(section.name)
            .let {
                val result = arrayListOf<Article>()
                it.forEach { a ->
                    result.add(map(a))
                }
                result
            }

    private fun map(a: ArticleEntity) =
        Article(
            section = a.section,
            subsection = a.subsection,
            title = a.title,
            abstract = a.abstractParagraph,
            uri = a.uri,
            url = a.url,
            byline = a.byline,
            item_type = a.item_type,
            updated_date = Date(a.updated_date),
            created_date = Date(a.created_date),
            published_date = Date(a.published_date),
            material_type_facet = a.material_type_facet,
            kicker = a.kicker,
            short_url = a.short_url
        )

    private fun map(a: Article, mainSection: String) =
        ArticleEntity(
            mainSection = mainSection,
            section = a.section,
            subsection = a.subsection,
            title = a.title,
            abstractParagraph = a.abstract,
            uri = a.uri,
            url = a.url,
            byline = a.byline,
            item_type = a.item_type,
            updated_date = a.updated_date.time,
            created_date = a.created_date.time,
            published_date = a.published_date.time,
            material_type_facet = a.material_type_facet,
            kicker = a.kicker,
            short_url = a.short_url
        )
}

