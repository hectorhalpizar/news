package me.hectorhalpizar.android.news.framework

import me.hectorhalpizar.android.news.framework.network.NewsRestApi
import me.hectorhalpizar.core.news.data.TopStoryDataSource
import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.domain.Section
import java.io.InputStream

class NetworkTopStoryDataSource(private val restApi: NewsRestApi) : TopStoryDataSource {
    override suspend fun store(article: Article, section: Section) {
        throw IllegalStateException("There is not a Network function to Store a Top Story article.")
    }

    override suspend fun delete(article: Article, section: Section) {
        throw IllegalStateException("There is not a Network function to Delete a Top Story article.")
    }

    override suspend fun deleteAllArticles(fromSection: Section) {
        throw IllegalStateException("There is not a Network function to Delete all the Top Stories articles.")
    }

    override suspend fun get(section: Section): List<Article> =
        restApi.getTopStories(section.sectionName.lowercase()).let { response ->
            if (response.isSuccessful) {
                response.body()?.results ?: run {
                    throw Error.Payload.BodyNull()
                }
            } else {
                response.errorBody()?.let {
                    throw Error.ErrorBody(response.code(), it.byteStream())
                } ?: run {
                    throw Error.Payload.ErrorBodyNull()
                }
            }
        }

    sealed class Error(message: String?, cause: Throwable?) : Exception(message, cause) {
        sealed class Payload : Error(null, null) {
            class BodyNull : Payload()
            class ErrorBodyNull : Payload()
        }
        class ErrorBody(val errorCode: Int, val errorStream: InputStream) : Error(null, null)
    }
}