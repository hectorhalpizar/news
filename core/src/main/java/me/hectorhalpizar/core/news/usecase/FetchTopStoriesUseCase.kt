package me.hectorhalpizar.core.news.usecase

import me.hectorhalpizar.core.news.data.TopStoryRepository
import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.domain.Section

class FetchTopStoriesUseCase(
    private val repository: TopStoryRepository
) : BaseUseCase<Section, List<Article>> {
    override suspend fun invoke(input: Section): List<Article> =
        try {
            repository.getRemoteTopStories(input).let { articles ->
                articles
                    .ifEmpty {
                        throw Error.Handled.RemoteTopStoriesEmpty()
                    }
                    try {
                        repository.storeTopStoriesOnDevice(articles, input)
                    } catch (e: Exception) {
                       return@let articles
                    }
                articles
            }
        } catch (e: Exception) {
            try {
                repository.getStoredTopStories(input).let { result ->
                    result.ifEmpty { throw Exception(e) }
                }
            } catch (exceptionOnFetchingFromDb: Exception) {
                throw Error.Caused(exceptionOnFetchingFromDb)
            }
        }

    sealed class Error(cause: Throwable?) : BaseUseCase.Error(null, cause) {
        internal sealed class Handled : Error(null) {
            internal class RemoteTopStoriesEmpty : Handled()
        }

        class Caused(cause: Throwable?) : Error(cause)
    }
}