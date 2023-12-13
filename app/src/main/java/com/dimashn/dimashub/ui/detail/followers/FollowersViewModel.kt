package com.dimashn.dimashub.ui.detail.followers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimashn.dimashub.data.remote.User
import com.dimashn.dimashub.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val followersList = MutableLiveData<ArrayList<User>>()
    val followers: LiveData<ArrayList<User>> = followersList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchFollowers(username: String) {
        _loading.value = true

        ApiService.create()
            .getFollowers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                    _loading.value = false
                    if (response.isSuccessful) {
                        followersList.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    _loading.value = false
                    Log.d("FetchFollowersFailure", t.message.toString())
                }
            })
    }

}
