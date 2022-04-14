package me.hectorhalpizar.core.nytimes.domain

import java.util.*

class Article(
    val section: String,
    val subsection: String,
    val title: String,
    val abstract: String,
    val url: String,
    val uri: String,
    val byline: String,
    val item_type: String,
    val updated_date: Date,
    val created_date: Date,
    val published_date: Date,
    val material_type_facet: String,
    val kicker: String,
    val des_facet: List<String>,
    val org_facet: List<String>,
    val per_facet: List<String>,
    val geo_facet: List<String>,
    val multimedia: List<Multimedia>,
    val short_url: String
)