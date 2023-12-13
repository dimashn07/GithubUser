package com.dimashn.dimashub.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class],
    version = 1
)
abstract class UserFavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): UserFavoriteDao

    companion object {
        private const val DATABASE_NAME = "dimashub.db"

        private var instance: UserFavoriteDatabase? = null

        fun getInstance(context: Context): UserFavoriteDatabase {
            return instance ?: synchronized(this) {
                instance ?: createDatabase(context).also { instance = it }
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context, UserFavoriteDatabase::class.java, DATABASE_NAME).build()
    }
}
