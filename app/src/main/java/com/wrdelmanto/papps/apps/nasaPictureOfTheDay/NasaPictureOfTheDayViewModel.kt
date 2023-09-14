package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.SP_NPOTD_AUTOMATIC_DOWNLOAD
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NasaPictureOfTheDayViewModel : ViewModel() {
    private val _title = MutableLiveData("")
    val title: MutableLiveData<String>
        get() = _title

    private val _description = MutableLiveData("")
    val description: MutableLiveData<String>
        get() = _description

    private val _copyright = MutableLiveData("")
    val copyright: MutableLiveData<String>
        get() = _copyright

    private val _date = MutableLiveData("")
    val date: MutableLiveData<String>
        get() = _date

    private val _url = MutableLiveData("")
    val url: MutableLiveData<String>
        get() = _url

    private val _hdurl = MutableLiveData("")
    val hdurl: MutableLiveData<String>
        get() = _hdurl

    private val _mediaType = MutableLiveData("")
    val mediaType: MutableLiveData<String>
        get() = _mediaType

    private val _state = MutableLiveData<NasaPictureOfTheDayViewModelState>()
    val state: MutableLiveData<NasaPictureOfTheDayViewModelState>
        get() = _state

    private val _automaticDownload = MutableLiveData(false)
    val automaticDownload: MutableLiveData<Boolean>
        get() = _automaticDownload

    private val _shouldDownloadPicture = MutableLiveData(true)
    val shouldDownloadPicture: MutableLiveData<Boolean>
        get() = _shouldDownloadPicture

    private lateinit var nasaPictureOfTheDayState: NasaPictureOfTheDayState

    private lateinit var nasaPictureOfTheDayData: NasaPictureOfTheDayData

    private fun setLoadingState() {
        nasaPictureOfTheDayState = NasaPictureOfTheDayState.LOADING
        postState()
    }

    private fun setLoadedState() {
        nasaPictureOfTheDayState = NasaPictureOfTheDayState.LOADED
        postState()
    }

    private fun setErrorState() {
        nasaPictureOfTheDayState = NasaPictureOfTheDayState.ERROR
        postState()
    }

    private fun postState() =
        _state.postValue(NasaPictureOfTheDayViewModelState(nasaPictureOfTheDayState))

    fun resetUi(context: Context) {
        _automaticDownload.value = SP_NPOTD_AUTOMATIC_DOWNLOAD.let {
            getSharedPreferences(context, it, Boolean) ?: false
        }.toString().toBoolean()

        getNasaData()
    }

    fun getNasaData() {
        setLoadingState()

        MainScope().launch {
            try {
                nasaPictureOfTheDayData =
                    NasaPictureOfTheDayApi.retrofitService.getNasaPictureOfTheDayData(
                        NASA_PICTURE_OF_THE_DAY_API_KEY
                    )

                _title.value = nasaPictureOfTheDayData.title
                _description.value = nasaPictureOfTheDayData.explanation
                _copyright.value = nasaPictureOfTheDayData.copyright
                _date.value = nasaPictureOfTheDayData.date
                _url.value = nasaPictureOfTheDayData.url
                _hdurl.value = nasaPictureOfTheDayData.hdUrl
                _mediaType.value = nasaPictureOfTheDayData.mediaType

                logD {
                    "title=${_title.value}, description=${_description.value}, copyright=${_copyright.value}, date=${_date.value}, url=${_url.value}, hdurl=${_hdurl.value}, mediaType=${mediaType.value}, serviceVersion=${nasaPictureOfTheDayData.version}, thumbnail_url=${nasaPictureOfTheDayData.thumbnailUrl}"
                }

                setLoadedState()
            } catch (e: Exception) {
                setErrorState()
                logD { e.toString() }
            }
        }
    }
}