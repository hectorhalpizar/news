package me.hectorhalpizar.android.news

import android.app.Application
import me.hectorhalpizar.android.news.framework.NetworkTopStoryDataSource
import me.hectorhalpizar.android.news.framework.NewsViewModelFactory
import me.hectorhalpizar.android.news.framework.RoomTopStoryDataSource
import me.hectorhalpizar.android.news.framework.network.NetworkService
import me.hectorhalpizar.core.news.data.TopStoryRepository
import me.hectorhalpizar.core.news.usecase.FetchTopStoriesUseCase
import me.hectorhalpizar.core.news.usecase.Interactor

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val local = RoomTopStoryDataSource(this.applicationContext)
        val remote = NetworkTopStoryDataSource(NetworkService.nyTimes)
        val repository = TopStoryRepository(local, remote)

        NewsViewModelFactory.inject(Interactor(FetchTopStoriesUseCase(repository)))
    }
}