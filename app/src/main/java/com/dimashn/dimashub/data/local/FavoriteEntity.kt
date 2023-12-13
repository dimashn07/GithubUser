package com.dimashn.dimashub.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_entity")
data class FavoriteEntity(
    @PrimaryKey
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String
) : Serializable