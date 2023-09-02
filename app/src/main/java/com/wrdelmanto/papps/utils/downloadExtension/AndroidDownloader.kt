package com.wrdelmanto.papps.utils.downloadExtension

import android.app.DownloadManager
import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.app.DownloadManager.EXTRA_DOWNLOAD_ID
import android.app.DownloadManager.Request.NETWORK_MOBILE
import android.app.DownloadManager.Request.NETWORK_WIFI
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData

class AndroidDownloader(private val context: Context) : Downloader {
    private val _state = MutableLiveData<DownloadViewModelState>()
    val state: MutableLiveData<DownloadViewModelState>
        get() = _state

    private lateinit var title: String
    private lateinit var description: String

    private lateinit var downloadState: DownloadState
    private var downloadId: Long = -1

    @RequiresApi(Build.VERSION_CODES.M)
    private val downloadManager: DownloadManager =
        context.getSystemService(DownloadManager::class.java) as DownloadManager

    fun updateDownloadInfo(tempTitle: String, desc: String) {
        title = tempTitle
        description = desc
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun downloadFile(url: String): Long {
        val request =
            DownloadManager.Request(url.toUri()).setTitle(title).setDescription(description)
                .setMimeType("image/jpeg").setAllowedOverRoaming(false)
                .setAllowedNetworkTypes(NETWORK_WIFI or NETWORK_MOBILE)
                .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .addRequestHeader("Authorization", "Bearer <token>")
                .setDestinationInExternalPublicDir(
                    DIRECTORY_DOWNLOADS, title
                )

        downloadId = downloadManager.enqueue(request)

        setDownloadingState()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                downloadReceiver, IntentFilter(ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_EXPORTED
            )
        }

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