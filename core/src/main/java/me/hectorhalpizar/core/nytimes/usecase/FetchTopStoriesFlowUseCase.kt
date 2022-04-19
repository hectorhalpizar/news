package me.hectorhalpizar.core.nytimes.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import me.hectorhalpizar.core.nytimes.data.TopStoryRepository
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section

class FetchTopStoriesFlowUseCase(
    private val repository: TopStoryRepository
) : BaseUseCase<Section, Flow<List<Article>>> {
    override fun invoke(input: Section): Flow<List<Article>> = flow {
        // TODO (Move Emit & Flow to the presentation layer)
        emit(
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
                e.printStackTrace()
                try {
                    repository.getStoredTopStories(input)
                } catch (storedException: Exception) {
                    throw Error.OnLocalRepository(storedException)
                }
            }
        )
    }.flowOn(Dispatchers.IO)


    sealed class Error(cause: Throwable?) : BaseUseCase.Error(null, cause) {
        internal sealed class Handled : Error(null) {
            internal class RemoteTopStoriesEmpty : Handled()
        }

        class OnLocalRepository(cause: Throwable?) : Error(cause)
    }
}