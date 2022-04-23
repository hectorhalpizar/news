package me.hectorhalpizar.android.news.framework.db

import androidx.room.Embedded
import androidx.room.Relation

data class ArticleAndMultimediaEntity(
    @Embedded val articleEntity: ArticleEntity,
    @Relation(parentColumn = "uri", entityColumn = "multimedia_uri") val multimediaEntity: List<MultimediaEntity> = emptyList()
)