package me.hectorhalpizar.android.news.framework

import androidx.lifecycle.ViewModel
import me.hectorhalpizar.core.news.usecase.Interactor

open class NewsViewModel(protected val interactor: Interactor) : ViewModel()