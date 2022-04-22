package me.hectorhalpizar.core.news.domain

import java.util.*

data class Article(
    val section: String,
    val subsection: String,
    val title: String,
    val abstract: String,
    val url: String,
    val uri: String = DEFAULT_URI_VALUE,
    val byline: String,
    val item_type: String,
    val updated_date: Date,
    val created_date: Date,
    val published_date: Date,
    val material_type_facet: String,
    val kicker: String,
    val short_url: String
) {
    var multimedia: List<Multimedia>? = null

    companion object {
        private const val DEFAULT_URI_VALUE = ""
    }
}