package me.hectorhalpizar.android.news.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.hectorhalpizar.android.news.framework.NewsViewModel
import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.domain.Section
import me.hectorhalpizar.core.news.update
import me.hectorhalpizar.core.news.usecase.FetchTopStoriesFlowUseCase
import me.hectorhalpizar.core.news.usecase.Interactor

class TopStoriesViewModel(interactor: Interactor) : NewsViewModel(interactor) {
    private val cachedTopStories : MutableList<Article> = mutableListOf()
    private val _fetchingState : MutableLiveData<RequestState> = MutableLiveData(RequestState.Initialized)
    val fetchingState: LiveData<RequestState> = _fetchingState

    fun fetch(section: Section) {
        viewModelScope.launch {
            _fetchingState.postValue(RequestState.Fetching)
            try {
                interactor.fetchTopStoriesFlowUseCase(section).let {
                    cachedTopStories.update(it)
                }
                _fetchingState.postValue(RequestState.Successful(cachedTopStories))
            } catch (e: FetchTopStoriesFlowUseCase.Error.Caused) {
                _fetchingState.postValue(RequestState.Failed(e.cause))
            }
        }
    }

    sealed class RequestState {
        object Initialized : RequestState()
        object Fetching : RequestState()
        data class Successful(val topStories: MutableList<Article>) : RequestState()
        data class Failed(val cause: Throwable?) : RequestState()
    }
}