package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val NASA_PICTURE_OF_THE_DAY_BASE_URL = "https://api.nasa.gov/planetary/"
const val NASA_PICTURE_OF_THE_DAY_API_KEY = "sdV5nVTPFuigmh7gySTWgg4IiLCRl95gjbdzGiGZ"

/**
 * Retrofit service object for creating api calls
 */
interface NasaApiService {
    @GET("apod")
    suspend fun getNasaPictureOfTheDayData(
        @Query("api_key") apiKey: String, @Query("thumbs") thumbs: Boolean = true
    ): NasaPictureOfTheDayData
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object NasaPictureOfTheDayApi {
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(NASA_PICTURE_OF_THE_DAY_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val retrofitService: NasaApiService = retrofit.create(NasaApiService::class.java)
}