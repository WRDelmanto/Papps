package com.wrdelmanto.papps.apps.random.randomQuote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.SP_RQ_AUTHOR
import com.wrdelmanto.papps.utils.SP_RQ_QUOTE
import com.wrdelmanto.papps.utils.checkInternetConnection
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.util.Locale

class RandomQuoteViewModel : ViewModel() {
    private val _currentQuote = MutableLiveData<String>()
    val currentQuote: LiveData<String> = _currentQuote

    private val _currentAuthor = MutableLiveData<String>()
    val currentAuthor: LiveData<String> = _currentAuthor

    private val _state = MutableLiveData<PensadorQuoteViewModelState>()
    val state: LiveData<PensadorQuoteViewModelState> = _state

    private lateinit var pensadorQuoteState: PensadorQuoteState

    private lateinit var pensadorData: PensadorData

    private fun setLoadingState() {
        pensadorQuoteState = PensadorQuoteState.LOADING
        postState()
    }

    private fun setLoadedState() {
        pensadorQuoteState = PensadorQuoteState.LOADED
        postState()
    }

    private fun setErrorState() {
        pensadorQuoteState = PensadorQuoteState.ERROR
        postState()
    }

    private fun postState() = _state.postValue(PensadorQuoteViewModelState(pensadorQuoteState))

    init {
        setLoadedState()
    }

    fun resetUi(context: Context) {
        _currentQuote.value = SP_RQ_QUOTE.let {
            val hs = getSharedPreferences(context, it, String)
            hs ?: ""
        }.toString()

        _currentAuthor.value = SP_RQ_AUTHOR.let {
            val hs = getSharedPreferences(context, it, String)
            hs ?: ""
        }.toString()

        generateNextQuote(context)
    }

    private fun generateNextQuote(context: Context) {
        if (checkInternetConnection(context) && pensadorQuoteState == PensadorQuoteState.LOADED) {
            MainScope().launch {
                setLoadingState()

                val generateNextAuthor = launch {
                    var tempAuthor: String

                    do tempAuthor = allAuthorsList.random()
                    while (tempAuthor == _currentAuthor.value)

                    _currentAuthor.value = tempAuthor
                }
                val generateNextQuote = launch {
                    do {
                        val numberOfRequestedQuotes = (1..20).random()

                        pensadorData = PensadorAPI.retrofitService.getPensadorData(
                            _currentAuthor.value.toString().replace(" ", "_")
                                .lowercase(Locale.ROOT), numberOfRequestedQuotes
                        )

                        _currentQuote.value = pensadorData.frases[numberOfRequestedQuotes - 1].texto
                    } while (pensadorData.frases[numberOfRequestedQuotes - 1].texto.length >= 500)
                }
                joinAll(generateNextAuthor, generateNextQuote)

                putSharedPreferences(context, SP_RQ_QUOTE, _currentQuote.value.toString())
                putSharedPreferences(context, SP_RQ_AUTHOR, _currentAuthor.value.toString())

                logD { "Author=${_currentAuthor.value}, termoDePesquisa=${pensadorData.termoDePesquisa}, total=${pensadorData.total}, Quote=${_currentQuote.value}" }

                setLoadedState()
            }
        } else setErrorState()
    }
}