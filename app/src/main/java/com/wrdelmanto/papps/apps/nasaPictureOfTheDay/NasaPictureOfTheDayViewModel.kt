package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wrdelmanto.papps.utils.SP_NPOTD_AUTOMATIC_DOWNLOAD
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NasaPictureOfTheDayViewModel : ViewModel() {
    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private val _copyright = MutableLiveData("")
    val copyright: LiveData<String> = _copyright

    private val _date = MutableLiveData("")
    val date: LiveData<String> = _date

    private val _url = MutableLiveData("")
    val url: LiveData<String> = _url

    private val _hdurl = MutableLiveData("")
    val hdurl: LiveData<String> = _hdurl

    private val _mediaType = MutableLiveData("")
    val mediaType: LiveData<String> = _mediaType

    private val _state = MutableLiveData<NasaPictureOfTheDayViewModelState>()
    val state: LiveData<NasaPictureOfTheDayViewModelState> = _state

    private val _automaticDownload = MutableLiveData(false)
    val automaticDownload: LiveData<Boolean> = _automaticDownload

    private val _shouldDownloadPicture = MutableLiveData(true)
    val shouldDownloadPicture: LiveData<Boolean> = _shouldDownloadPicture

    private var getNasaDataJob: Job? = null

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

    fun updateShouldDownloadPicture(newShouldDownloadPicture: Boolean) {
        _shouldDownloadPicture.value = newShouldDownloadPicture
    }

    fun updateAutomaticDownload(newautomaticDownload: Boolean) {
        _automaticDownload.value = newautomaticDownload
    }

    fun getNasaData() {
        setLoadingState()

        getNasaDataJob?.cancel()
        getNasaDataJob = viewModelScope.launch {
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