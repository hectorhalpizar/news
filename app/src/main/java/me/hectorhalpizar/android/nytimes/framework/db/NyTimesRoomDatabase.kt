package me.hectorhalpizar.android.nytimes.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ArticleEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NyTimesRoomDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {

        private const val DATABASE_NAME = "ny_times.db"

        // For Singleton instantiation
        @Volatile private var instance: NyTimesRoomDatabase? = null

        fun getInstance(context: Context): NyTimesRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NyTimesRoomDatabase {
            return Room.databaseBuilder(context, NyTimesRoomDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}