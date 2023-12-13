package com.dimashn.dimashub.network

import com.dimashn.BuildConfig
import com.dimashn.dimashub.data.remote.DetailUserResponse
import com.dimashn.dimashub.data.remote.User
import com.dimashn.dimashub.data.remote.UserResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {

        const val LIST_USER = "users"
        const val SEARCH_USER = "search/users"
        const val USER = "users/{username}"
        const val FOLLOWERS = "users/{username}/followers"
        const val FOLLOWING = "users/{username}/following"

        private const val BASE_URL = "https://api.github.com/"
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        const val mySuperSecretKey = BuildConfig.KEY

        fun create(): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }

    @GET(LIST_USER)
    @Headers("Authorization: token $mySuperSecretKey")
    fun getAllGitHubUsers(): Call<List<User>>

    @GET(SEARCH_USER)
    @Headers("Authorization: token $mySuperSecretKey")
    fun getSearchUser(@Query("q") query: String): Call<UserResponse>

    @GET(USER)
    @Headers("Authorization: token $mySuperSecretKey")
    fun getUserDetail(@Path("username") username: String): Call<DetailUserResponse>

    @GET(FOLLOWERS)
    @Headers("Authorization: token $mySuperSecretKey")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<User>>

    @GET(FOLLOWING)
    @Headers("Authorization: token $mySuperSecretKey")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<User>>
}

