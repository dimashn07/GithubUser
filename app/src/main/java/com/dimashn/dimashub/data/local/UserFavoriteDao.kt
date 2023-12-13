package com.dimashn.dimashub.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserFavoriteDao {

    @Insert
    suspend fun insertUserFavorite(userFavorite: FavoriteEntity)

    @Query("SELECT * FROM favorite_entity")
    fun getAllUserFavorites(): LiveData<List<FavoriteEntity>>

    @Query("SELECT count(*) FROM favorite_entity WHERE id = :userId")
    suspend fun getUserFavoriteCount(userId: Int): Int

    @Query("DELETE FROM favorite_entity WHERE id = :userId")
    suspend fun deleteUserFavorite(userId: Int)
}
