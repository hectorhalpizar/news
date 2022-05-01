package me.hectorhalpizar.android.news.model

data class TutorialInfo(
    val page: Int,
    val informationStringId: Int,
    val lastPage: Boolean = false,
    val arrowId: Int
)
