package me.hectorhalpizar.android.nytimes.framework

import me.hectorhalpizar.android.nytimes.framework.network.NyTimesRestApi
import me.hectorhalpizar.core.nytimes.data.TopStoryDataSource
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section
import java.io.InputStream

class NetworkTopStoryDataSource(private val restApi: NyTimesRestApi) : TopStoryDataSource {
    override fun store(article: Article, section: Section) {
        throw IllegalStateException("There is not a Network function to Store an Top Story article.")
    }

    override fun get(section: Section): List<Article> =
        restApi.getTopStories(section.name.lowercase()).execute().let { response ->
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