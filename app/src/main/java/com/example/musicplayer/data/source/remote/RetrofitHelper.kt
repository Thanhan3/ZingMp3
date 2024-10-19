package com.example.musicplayer.data.source.remote

import com.google.gson.Gson
import retrofit2.Retrofit

object RetrofitHelper {
    val instance: MusicService
        get(){
            val baseUrl = "https://thantrieu.com"
            return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(Gson)
        }

}