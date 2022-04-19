package me.hectorhalpizar.android.nytimes.framework.network

import me.hectorhalpizar.android.nytimes.BuildConfig.NY_TIMES_REST_API_KEY
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NyTimesRestApi {
    @GET("topstories/v2/{section}.json")
    suspend fun getTopStories(@Path("section") section: String, @Query("api-key") apiKey: String = NY_TIMES_REST_API_KEY) : Response<Result>
}