package com.dimashn.dimashub.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimashn.dimashub.data.remote.User
import com.dimashn.dimashub.data.remote.UserResponse
import com.dimashn.dimashub.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    private val isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = isLoadingLiveData

    init {
        isLoadingLiveData.value = false
    }

    fun setSearchUsers(query: String) {

        if (listUsers.value == null) {
            listUsers.value = ArrayList()
        }
        isLoadingLiveData.value = true
        ApiService.create()
            .getSearchUser(query)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    isLoadingLiveData.value = false
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    isLoadingLiveData.value = false
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getSearchUser(): LiveData<ArrayList<User>> {
        return listUsers
    }

    fun clearSearchUser() {
        listUsers.value = ArrayList()
    }
}