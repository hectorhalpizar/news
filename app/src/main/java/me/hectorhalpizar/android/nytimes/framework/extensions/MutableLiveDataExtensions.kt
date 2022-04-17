package me.hectorhalpizar.android.nytimes.framework.extensions

import androidx.lifecycle.MutableLiveData

operator fun <T> MutableLiveData<List<T>>.plusAssign(itemList: List<T>) {
    val value = this.value ?: emptyList()
    val list = arrayListOf<T>().apply {
        addAll(itemList)
    }.toList()
    this.value = value + list
}