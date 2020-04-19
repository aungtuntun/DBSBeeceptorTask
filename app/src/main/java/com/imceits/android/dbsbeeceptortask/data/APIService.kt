package com.imceits.android.dbsbeeceptortask.data

import retrofit2.Call
import retrofit2.http.GET

interface APIService {

    @GET("article")
    fun getArticles(): Call<List<Article>>
}