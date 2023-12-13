package com.dimashn.dimashub.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dimashn.dimashub.data.local.UserFavoriteDao
import com.dimashn.dimashub.data.local.FavoriteEntity
import com.dimashn.dimashub.data.local.UserFavoriteDatabase
import com.dimashn.dimashub.data.remote.DetailUserResponse
import com.dimashn.dimashub.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> get() = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val dao: UserFavoriteDao?
    private val database: UserFavoriteDatabase?

    init {
        database = UserFavoriteDatabase.getInstance(application)
        dao = database.favoriteDao()
    }

    fun setDetailUser(username: String) {
        _isLoading.value = true
        ApiService.create().getUserDetail(username).enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                if (response.isSuccessful) {
                    _user.postValue(response.body())
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
                _isLoading.value = false
            }
        })
    }

    fun addToFavorite(avatarUrl: String, htmlUrl: String, id: Int, username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteEntity(id, username, avatarUrl, htmlUrl)
            dao?.insertUserFavorite(user)
        }
    }

    suspend fun checkUser(id: Int): Int? {
        return dao?.getUserFavoriteCount(id)
    }

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dao?.deleteUserFavorite(id)
        }
    }
}

