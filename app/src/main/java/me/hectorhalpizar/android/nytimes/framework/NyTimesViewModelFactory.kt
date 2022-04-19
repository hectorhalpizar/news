package me.hectorhalpizar.android.nytimes.framework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.hectorhalpizar.core.nytimes.usecase.Interactor

object NyTimesViewModelFactory : ViewModelProvider.Factory {
    private lateinit var interactor: Interactor

    fun inject(interactor: Interactor) {
        this.interactor = interactor
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(NyTimesViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass
                .getConstructor(Interactor::class.java)
                .newInstance(interactor)
        } else {
            throw IllegalStateException("ViewModel must extend NyTimesViewModel")
        }
    }
}