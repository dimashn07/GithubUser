package com.dimashn.dimashub.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.dimashn.dimashub.data.remote.User
import com.dimashn.dimashub.network.ApiService
import com.dimashn.dimashub.ui.setting.SettingsPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: SettingsPreferences) : ViewModel() {

    private val _listUsers: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val listUsers: LiveData<ArrayList<User>> = _listUsers

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getThemeSetting(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    fun getGitHubUsers(): LiveData<List<User>> {
        val result = MutableLiveData<List<User>>()
        _isLoading.value = true

        ApiService.create().getAllGitHubUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    result.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                Log.d("Failure", t.message.toString())
            }
        })

        return result
    }

}

