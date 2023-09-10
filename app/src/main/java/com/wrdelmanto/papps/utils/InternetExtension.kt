package com.wrdelmanto.papps.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import com.wrdelmanto.papps.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

const val INTERNET_DATA_URL = "http://ip-api.com/json/"

fun checkInternetConnection(context: Context): Boolean {
    return if (checkInternet(context)) {
        logD { context.resources.getString(R.string.internet_connection_found) }
        true
    } else {
        logD { context.resources.getString(R.string.no_internet_connection_warning) }
        showNormalToast(
            context, context.resources.getString(R.string.no_internet_connection_warning)
        )
        false
    }
}

@Suppress("DEPRECATION")
private fun checkInternet(context: Context): Boolean {
    val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (SDK_INT >= M) {
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        when {
            activeNetwork?.hasTransport(TRANSPORT_WIFI) == true -> true
            activeNetwork?.hasTransport(TRANSPORT_CELLULAR) == true -> true
            activeNetwork?.hasTransport(TRANSPORT_ETHERNET) == true -> true
            else -> false
        }
    } else connectivityManager.activeNetworkInfo?.isConnected == true
}

@Suppress("DEPRECATION")
fun getInternetStatus(context: Context): List<String> {
    val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkTypeName =
        connectivityManager.activeNetworkInfo?.typeName.toString().toLowerCase(Locale.ROOT)
            .capitalize(
                Locale.ROOT
            )

    return if (SDK_INT >= M) {
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        val downloadSpeed = (activeNetwork?.linkDownstreamBandwidthKbps)?.div(1000) ?: 0
        val uploadSpeed = (activeNetwork?.linkUpstreamBandwidthKbps)?.div(1000) ?: 0

        logD { "downloadSpeed=$downloadSpeed, uploadSpeed=$uploadSpeed, networkType=$networkTypeName" }

        listOf(downloadSpeed.toString(), uploadSpeed.toString(), networkTypeName)
    } else listOf("0", "0", networkTypeName)
}

suspend fun getInternetInformation(dispatcher: CoroutineDispatcher = Dispatchers.IO): List<String> {
    return withContext(dispatcher) {
        try {
            val response =
                OkHttpClient().newCall(Request.Builder().url(INTERNET_DATA_URL).build()).execute()

            if (response.isSuccessful) {
                val responseBody = response.body()?.string()

                if (responseBody != null) {
                    val status = JSONObject(responseBody).getString("status")
                    val contry = JSONObject(responseBody).getString("country")
                    val countryCode = JSONObject(responseBody).getString("countryCode")
                    val region = JSONObject(responseBody).getString("region")
                    val regionName = JSONObject(responseBody).getString("regionName")
                    val city = JSONObject(responseBody).getString("city")
                    val zip = JSONObject(responseBody).getString("zip")
                    val lat = JSONObject(responseBody).getString("lat")
                    val lon = JSONObject(responseBody).getString("lon")
                    val timezone = JSONObject(responseBody).getString("timezone")
                    val isp = JSONObject(responseBody).getString("isp")
                    val org = JSONObject(responseBody).getString("org")
                    val asNumber = JSONObject(responseBody).getString("as")
                    val query = JSONObject(responseBody).getString("query")

                    logD {
                        "status=$status, contry=$contry, countryCode=$countryCode, region=$region, regionName=$regionName, city=$city, zip=$zip, lat=$lat, lon=$lon,timezone=$timezone,isp=$isp,org=$org, asNumber=$asNumber, query=$query"
                    }

                    return@withContext listOf(
                        status,
                        contry,
                        countryCode,
                        region,
                        regionName,
                        city,
                        zip,
                        lat,
                        lon,
                        timezone,
                        isp,
                        org,
                        asNumber,
                        query
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        logD { "Unable to fetch internet information" }

        return@withContext listOf(
            "", "", "", "", "", "", "", "", "", "", "", "", "", ""
        )
    }
}