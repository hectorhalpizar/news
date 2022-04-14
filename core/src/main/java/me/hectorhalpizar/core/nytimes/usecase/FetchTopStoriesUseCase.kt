package me.hectorhalpizar.core.nytimes.usecase

import me.hectorhalpizar.core.nytimes.data.TopStoryRepository
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section

/**
 * Fetch top stories articles from the New York Times Rest API server.
 *
 * If there is a problem with the server it gets the cached top stories.
 *
 * If something there a problem fetching the articles stored in the device, the Use Case will throw a Fetching exception.
 */
class FetchTopStoriesUseCase(
    private val repository: TopStoryRepository
) : BaseUseCase<Section, List<Article>> {
    @Throws(Error.Unknown::class)
    override fun invoke(input: Section) : List<Article> =
        try {
            repository.getRemoteTopStories(input).let { articles ->
                articles
                    .ifEmpty {
                        throw Error.Handled.RemoteTopStoriesEmpty()
                    }
                    .forEach { article ->
                        try {
                            repository.storeTopStoryOnDevice(article)
                        } catch (e: Exception) {
                            return@let articles
                        }
                    }
                articles
            }
        } catch (e: Exception) {
            when(e) {
                is TopStoryRepository.Error.Network,
                is Error.Handled.RemoteTopStoriesEmpty -> {
                    repository.getStoredTopStories(input)
                }
                else -> {
                    throw Error.Unknown(e)
                }
            }
        }

    sealed class Error(message: String?, cause: Throwable?): BaseUseCase.Error(message, cause)  {
        internal sealed class Handled : Error(null, null) {
            internal class RemoteTopStoriesEmpty : Handled()
        }
        class Unknown(cause: Throwable?) : Error(null, cause)
    }
}