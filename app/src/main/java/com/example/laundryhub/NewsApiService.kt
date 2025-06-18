package com.example.laundryhub.Network

import com.example.laundryhub.Model.GNewsResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GNewsApi {

    @GET("v4/top-headlines")
    fun getTopHeadlines(
        @Query("token") token: String,
        @Query("lang") lang: String = "en"
    ): Call<GNewsResponse>

    companion object {
        private const val BASE_URL = "https://gnews.io/api/"

        fun create(): GNewsApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(GNewsApi::class.java)
        }
    }
}

