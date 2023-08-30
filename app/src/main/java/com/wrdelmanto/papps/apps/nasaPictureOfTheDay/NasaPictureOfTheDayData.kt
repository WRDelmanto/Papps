package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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