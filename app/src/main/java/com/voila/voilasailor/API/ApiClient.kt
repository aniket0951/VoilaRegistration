package com.voila.voilasailor.API

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    var apiService: ApiService? = null

    object RetrofitCall{
        val retrofit: ApiService = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(WebServer.BASE_URL)
                .build()
                .create(ApiService::class.java)
    }
}