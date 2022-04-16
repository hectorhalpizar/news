package me.hectorhalpizar.android.nytimes.framework.db

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import kotlin.reflect.typeOf

@Entity(tableName = "articles", primaryKeys = ["uri", "mainSection"])
data class ArticleEntity(
    val uri: String = DEFAULT_PRIMARY_KEY_VALUE,
    val mainSection: String,
    val section: String,
    val subsection: String,
    val title: String,
    val abstractParagraph: String,
    val url: String,
    val byline: String,
    val item_type: String,
    val updated_date: Long,
    val created_date: Long,
    val published_date: Long,
    val material_type_facet: String,
    val kicker: String,
    val short_url: String
) {
    fun size() {
        uri::class.simpleName
    }
    companion object {
        private const val DEFAULT_PRIMARY_KEY_VALUE = ""
    }
}