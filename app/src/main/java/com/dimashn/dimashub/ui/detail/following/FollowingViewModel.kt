package com.dimashn.dimashub.ui.detail.following

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimashn.dimashub.data.remote.User
import com.dimashn.dimashub.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val followingList = MutableLiveData<ArrayList<User>>()
    val following: LiveData<ArrayList<User>> = followingList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchFollowing(username: String) {
        _loading.value = true

        ApiService.create()
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                    _loading.value = false
                    if (response.isSuccessful) {
                        followingList.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    _loading.value = false
                    Log.d("FetchFollowingFailure", t.message.toString())
                }
            })
    }

}
