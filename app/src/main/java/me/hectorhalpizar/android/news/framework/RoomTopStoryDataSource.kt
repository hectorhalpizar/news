package me.hectorhalpizar.android.news.framework

import android.content.Context
import android.util.Log
import me.hectorhalpizar.android.news.framework.db.*
import me.hectorhalpizar.core.news.data.TopStoryDataSource
import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.domain.Multimedia
import me.hectorhalpizar.core.news.domain.Section
import java.util.*
import kotlin.collections.ArrayList

class RoomTopStoryDataSource internal constructor(database: NewsRoomDatabase): TopStoryDataSource {

    constructor(context: Context) : this(NewsRoomDatabase.getInstance(context))

    private val articleDao: ArticleDao = database.articleDao()
    private val multimediaDao: MultimediaDao = database.multimediaDao()

    override suspend fun store(article: Article, section: Section) {
        article.let { a ->
            articleDao.addArticle(map(a, mainSection = section.sectionName))
            a.multimedia?.forEach { m -> multimediaDao.addMultimedia(map(a.uri, m)) }
        }
    }

    override suspend fun get(section: Section): List<Article> =
        articleDao.getTopStoriesWithMultimedia(section.sectionName)
            .let {
                val result = arrayListOf<Article>()
                it.forEach { entity ->
                    val multimedia: ArrayList<Multimedia> = arrayListOf()
                    entity.multimediaEntity.forEach { m -> multimedia.add(map(m)) }
                    result.add(map(entity.articleEntity, multimedia))
                }
                result
            }

    override suspend fun delete(article: Article, section: Section) {
        articleDao.deleteArticle(map(article, section.sectionName))
    }

    override suspend fun deleteAllArticles(fromSection: Section) {
        articleDao.deleteAllArticles(fromSection.sectionName)
    }

    private fun map(a: ArticleEntity, multimedia: List<Multimedia> = emptyList()) =
        Article(
            section = a.section,
            subsection = a.subsection,
            title = a.title,
            abstract = a.abstractParagraph,
            uri = a.uri,
            url = a.url,
            byline = a.byline,
            item_type = a.item_type,
            updated_date = a.updated_date?.let { Date(it) },
            created_date = a.created_date?.let { Date(it) },
            published_date = a.published_date?.let { Date(it) },
            material_type_facet = a.material_type_facet,
            kicker = a.kicker,
            short_url = a.short_url,
            multimedia = multimedia
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
            updated_date = a.updated_date?.time,
            created_date = a.created_date?.time,
            published_date = a.published_date?.time,
            material_type_facet = a.material_type_facet,
            kicker = a.kicker,
            short_url = a.short_url,
        )

    private fun map(uri: String, m: Multimedia) =
        MultimediaEntity(
            uri = uri,
            url = m.url,
            format = m.format,
            height = m.height,
            width = m.width,
            type = m.type,
            subtype = m.subtype,
            copyright = m.copyright,
            caption = m.caption
        )

    private fun map(m: MultimediaEntity) =
        Multimedia(
            url = m.url,
            format = m.format,
            height = m.height,
            width = m.width,
            type = m.type,
            subtype = m.subtype,
            caption = m.caption,
            copyright = m.copyright
        )
}

