package me.hectorhalpizar.android.news.framework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.hectorhalpizar.core.news.usecase.Interactor

object NewsViewModelFactory : ViewModelProvider.Factory {
    private lateinit var interactor: Interactor

    fun inject(interactor: Interactor) {
        this.interactor = interactor
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(NewsViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass
                .getConstructor(Interactor::class.java)
                .newInstance(interactor)
        } else {
            throw IllegalStateException("ViewModel must extend NyTimesViewModel")
        }
    }
}