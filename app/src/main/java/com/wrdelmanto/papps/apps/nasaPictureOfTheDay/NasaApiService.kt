package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.nasa.gov/planetary/apod?api_key=sdV5nVTPFuigmh7gySTWgg4IiLCRl95gjbdzGiGZ

private const val NASA_PICTURE_OF_THE_DAY_BASE_URL = "https://api.nasa.gov/planetary/"
const val NASA_PICTURE_OF_THE_DAY_API_KEY = "sdV5nVTPFuigmh7gySTWgg4IiLCRl95gjbdzGiGZ"

@Parcelize
data class NasaPictureOfTheDayData(
    @SerializedName("copyright") @Expose val copyright: String,
    @SerializedName("date") @Expose val date: String,
    @SerializedName("explanation") @Expose val explanation: String,
    @SerializedName("hdurl") @Expose val hdUrl: String,
    @SerializedName("media_type") @Expose val mediaType: String,
    @SerializedName("service_version") @Expose val version: String,
    @SerializedName("title") @Expose val title: String,
    @SerializedName("url") @Expose val url: String,
    @SerializedName("thumbnail_url") @Expose val thumbnailUrl: String
) : Parcelable

interface NasaApiService {
    @GET("apod")
    suspend fun getNasaPictureOfTheDayData(@Query("api_key") apiKey: String): NasaPictureOfTheDayData
}

object NasaPictureOfTheDayApi {
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(NASA_PICTURE_OF_THE_DAY_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val retrofitService: NasaApiService = retrofit.create(NasaApiService::class.java)
}