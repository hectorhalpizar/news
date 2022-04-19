package me.hectorhalpizar.android.nytimes

import android.app.Application
import me.hectorhalpizar.android.nytimes.framework.NetworkTopStoryDataSource
import me.hectorhalpizar.android.nytimes.framework.NyTimesViewModelFactory
import me.hectorhalpizar.android.nytimes.framework.RoomTopStoryDataSource
import me.hectorhalpizar.android.nytimes.framework.network.NetworkService
import me.hectorhalpizar.core.nytimes.data.TopStoryRepository
import me.hectorhalpizar.core.nytimes.usecase.FetchTopStoriesFlowUseCase
import me.hectorhalpizar.core.nytimes.usecase.Interactor

class NyTimesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val local = RoomTopStoryDataSource(this.applicationContext)
        val remote = NetworkTopStoryDataSource(NetworkService.nyTimes)
        val repository = TopStoryRepository(local, remote)

        NyTimesViewModelFactory.inject(Interactor(FetchTopStoriesFlowUseCase(repository)))
    }
}