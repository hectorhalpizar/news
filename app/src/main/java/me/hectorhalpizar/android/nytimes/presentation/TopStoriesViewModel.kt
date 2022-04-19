package me.hectorhalpizar.android.nytimes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.hectorhalpizar.android.nytimes.framework.NyTimesViewModel
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.domain.Section
import me.hectorhalpizar.core.nytimes.usecase.Interactor

class TopStoriesViewModel(interactor: Interactor) : NyTimesViewModel(interactor) {
    private val _articles: MutableLiveData<MutableMap<String, Article>> = MutableLiveData(mutableMapOf())
    val articles: LiveData<MutableMap<String, Article>> = _articles

    fun fetch(section: Section) {
        interactor.fetchTopStories(section).let { feed ->
            feed.forEach { article ->
                articles.value?.set(article.uri, article)
            }
            _articles.postValue(articles.value)
        }
    }
}