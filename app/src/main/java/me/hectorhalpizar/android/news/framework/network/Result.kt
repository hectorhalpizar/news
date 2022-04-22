package me.hectorhalpizar.android.news.framework.network

import me.hectorhalpizar.core.news.domain.Article
import java.util.*

data class Result(
    val status: String,
    val copyright: String,
    val section: String,
    val last_updated: Date,
    val num_results: Int,
    val results: List<Article>
)