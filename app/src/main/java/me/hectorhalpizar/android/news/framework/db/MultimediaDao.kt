package me.hectorhalpizar.android.news.framework.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface MultimediaDao {
    @Insert(onConflict = REPLACE)
    suspend fun addMultimedia(multimedia: MultimediaEntity)

    @Delete
    suspend fun deleteMultimedia(multimedia: MultimediaEntity)
}