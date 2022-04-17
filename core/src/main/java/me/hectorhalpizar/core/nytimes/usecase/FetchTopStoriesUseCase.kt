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
    override fun invoke(input: Section) : List<Article> =
        try {
            repository.getRemoteTopStories(input).let { articles ->
                articles
                    .ifEmpty {
                        throw Error.Handled.RemoteTopStoriesEmpty()
                    }
                    .forEach { article ->
                        try {
                            repository.storeTopStoryOnDevice(article, input)
                        } catch (e: Exception) {
                            return@let articles
                        }
                    }
                articles
            }
        } catch (e: Exception) {
            try {
                repository.getStoredTopStories(input)
            } catch (storedException: Exception) {
                throw Error.OnLocalRepository(storedException.cause)
            }
        }

    sealed class Error(cause: Throwable?): BaseUseCase.Error(null, cause)  {
        internal sealed class Handled : Error(null) {
            internal class RemoteTopStoriesEmpty : Handled()
        }
        class OnLocalRepository(cause: Throwable?) : Error(cause)
    }
}