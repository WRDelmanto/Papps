package com.wrdelmanto.papps.utils.downloadExtension

import android.app.DownloadManager
import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.app.DownloadManager.EXTRA_DOWNLOAD_ID
import android.app.DownloadManager.Request.NETWORK_MOBILE
import android.app.DownloadManager.Request.NETWORK_WIFI
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.wrdelmanto.papps.R

class AndroidDownloader(private val context: Context) : Downloader {
    private val _state = MutableLiveData<DownloadViewModelState>()
    val state: MutableLiveData<DownloadViewModelState>
        get() = _state

    private lateinit var title: String

    private lateinit var downloadState: DownloadState
    private var downloadId: Long = -1

    @RequiresApi(Build.VERSION_CODES.M)
    private val downloadManager: DownloadManager =
        context.getSystemService(DownloadManager::class.java) as DownloadManager

    fun updateDownloadInfo(contentTitle: String) {
        title = contentTitle
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun downloadFile(url: String): Long {

        val request = DownloadManager.Request(url.toUri()).setTitle(title).setDescription(
            context.resources.getString(
                R.string.downloading
            )
        ).setMimeType("image/jpeg").setAllowedOverRoaming(false).setAllowedOverMetered(true)
            .setAllowedNetworkTypes(NETWORK_WIFI or NETWORK_MOBILE)
            .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .addRequestHeader("Authorization", "Bearer <token>").setDestinationInExternalPublicDir(
                DIRECTORY_DOWNLOADS, title
            )

        downloadId = downloadManager.enqueue(request)

        setDownloadingState()

        if (SDK_INT >= TIRAMISU) context.registerReceiver(
            downloadReceiver, IntentFilter(ACTION_DOWNLOAD_COMPLETE), RECEIVER_EXPORTED
        )

        return downloadId
    }

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_DOWNLOAD_COMPLETE && intent.getLongExtra(
                    EXTRA_DOWNLOAD_ID, -1
                ) == downloadId
            ) {
                context?.unregisterReceiver(this)

                setDownloadedState()

                return
            }
        }
    }

    private fun setDownloadingState() {
        downloadState = DownloadState.DOWNLOADING
        postState()
    }

    private fun setDownloadedState() {
        downloadState = DownloadState.DOWNLOADED
        postState()
    }

    private fun postState() = state.postValue(DownloadViewModelState(downloadState))
}