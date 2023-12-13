package com.dimashn.dimashub.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dimashn.dimashub.data.local.UserFavoriteDao
import com.dimashn.dimashub.data.local.FavoriteEntity
import com.dimashn.dimashub.data.local.UserFavoriteDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: UserFavoriteDao = UserFavoriteDatabase.getInstance(application).favoriteDao()

    fun getFavoriteUser(): LiveData<List<FavoriteEntity>> {
        return dao.getAllUserFavorites()
    }
}
