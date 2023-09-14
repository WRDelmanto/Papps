package com.wrdelmanto.papps.apps.random.randomQuote

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Example: https://pensador-api.vercel.app/?term=fernando+pessoa&max=2

private const val PENSADOR_API_VERCEL_URL = "https://pensador-api.vercel.app/"

@Parcelize
data class PensadorData(
    @SerializedName("termoDePesquisa") @Expose val termoDePesquisa: String,
    @SerializedName("total") @Expose val total: Int,
    @SerializedName("frases") @Expose val frases: List<QuoteData>
) : Parcelable

@Parcelize
data class QuoteData(
    val autor: String, val texto: String
) : Parcelable

fun interface PensadorApiService {
    @GET(PENSADOR_API_VERCEL_URL)
    suspend fun getPensadorData(
        @Query("term") term: String, @Query("max") max: Int
    ): PensadorData
}

object PensadorAPI {
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(PENSADOR_API_VERCEL_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val retrofitService: PensadorApiService = retrofit.create(PensadorApiService::class.java)
}