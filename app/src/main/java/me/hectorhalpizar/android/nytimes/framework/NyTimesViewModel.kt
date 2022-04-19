package me.hectorhalpizar.android.nytimes.framework

import androidx.lifecycle.ViewModel
import me.hectorhalpizar.core.nytimes.usecase.Interactor

open class NyTimesViewModel(protected val interactor: Interactor) : ViewModel()