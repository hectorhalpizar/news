package me.hectorhalpizar.android.nytimes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section
import me.hectorhalpizar.core.nytimes.usecase.FetchTopStoriesUseCase

class TopStoriesViewModel(
    private val fetchTopStories: FetchTopStoriesUseCase
) : ViewModel() {
    private val _articles: MutableLiveData<MutableMap<String, Article>> = MutableLiveData(mutableMapOf())
    val articles: LiveData<MutableMap<String, Article>> = _articles


    fun fetch(section: Section) {
        fetchTopStories(section).let { feed ->
            feed.forEach { article ->
                articles.value?.set(article.uri, article)
            }
            _articles.postValue(articles.value)
        }
    }
}