package com.wrdelmanto.papps.apps.speedTest

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.checkInternetConnection
import com.wrdelmanto.papps.utils.getInternetInformation
import com.wrdelmanto.papps.utils.getInternetStatus
import com.wrdelmanto.papps.utils.logD
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SpeedTestViewModel : ViewModel() {
    private val _download = MutableLiveData<String>()
    val download: LiveData<String> = _download

    private val _upload = MutableLiveData<String>()
    val upload: LiveData<String> = _upload

    private val _networkTypeName = MutableLiveData<String>()
    val networkTypeName: LiveData<String> = _networkTypeName

    private val _publicIpAddress = MutableLiveData<String>()
    val publicIpAddress: LiveData<String> = _publicIpAddress

    private val _networkRegion = MutableLiveData<String>()
    val networkRegion: LiveData<String> = _networkRegion

    fun resetUi(context: Context) {
        if (checkInternetConnection(context)) {
            MainScope().launch {
                val internetStatus = getInternetStatus(context)
                val internetInformation = getInternetInformation()

                _download.value = internetStatus[0]
                _upload.value = internetStatus[1]
                _networkTypeName.value = internetStatus[2] + " - " + internetInformation[10]
                _publicIpAddress.value = internetInformation[13]
                _networkRegion.value = internetInformation[1] + " - " + internetInformation[4]

                logD { "Download=${_download.value}, Upload=${_upload.value}, networkType=${_networkTypeName.value}, publicIpAddress=${_publicIpAddress.value}, networkRegion=${_networkRegion.value}" }
            }
        } else {
            _download.value = "?"
            _upload.value = "?"
            _networkTypeName.value = "? - ?"
            _publicIpAddress.value = "?"
            _networkRegion.value = "? - ?"
        }

        logD { "resetUi" }
    }
}