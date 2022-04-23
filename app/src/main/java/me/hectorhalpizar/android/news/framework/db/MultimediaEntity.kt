package me.hectorhalpizar.android.news.framework.db

import androidx.room.*

@Entity(tableName = "multimedia",
    foreignKeys = [ForeignKey(entity = ArticleEntity::class, parentColumns = ["uri"], childColumns = ["multimedia_uri"])],
    indices = [Index("multimedia_uri")]
)
data class MultimediaEntity(
    @PrimaryKey @ColumnInfo(name="multimedia_uri") val uri: String = DEFAULT_PRIMARY_KEY_VALUE,
    val url: String,
    val format: String,
    val height: Int,
    val width: Int,
    val type: String,
    val subtype: String,
    val caption: String,
    val copyright: String
) {
    companion object {
        private const val DEFAULT_PRIMARY_KEY_VALUE = ""
    }
}