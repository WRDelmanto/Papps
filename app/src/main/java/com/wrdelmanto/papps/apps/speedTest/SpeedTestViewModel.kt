package com.wrdelmanto.papps.apps.speedTest

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.checkInternetConnection
import com.wrdelmanto.papps.utils.getInternetInformation
import com.wrdelmanto.papps.utils.getInternetStatus
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SpeedTestViewModel : ViewModel() {
    private val _download = MutableLiveData<String>()
    val download: MutableLiveData<String>
        get() = _download

    private val _upload = MutableLiveData<String>()
    val upload: MutableLiveData<String>
        get() = _upload

    private val _networkTypeName = MutableLiveData<String>()
    val networkTypeName: MutableLiveData<String>
        get() = _networkTypeName

    private val _publicIpAddress = MutableLiveData<String>()
    val publicIpAddress: MutableLiveData<String>
        get() = _publicIpAddress

    private val _networkRegion = MutableLiveData<String>()
    val networkRegion: MutableLiveData<String>
        get() = _networkRegion

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
            }
        } else {
            _download.value = "?"
            _upload.value = "?"
            _networkTypeName.value = "? - ?"
            _publicIpAddress.value = "?"
            _networkRegion.value = "? - ?"
        }
    }
}