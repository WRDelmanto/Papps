package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentNasaPictureOfTheDayBinding
import com.wrdelmanto.papps.utils.SP_NPOTD_AUTOMATIC_DOWNLOAD
import com.wrdelmanto.papps.utils.checkInternetConnection
import com.wrdelmanto.papps.utils.downloadExtension.AndroidDownloader
import com.wrdelmanto.papps.utils.downloadExtension.DownloadState
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.showNormalToast
import com.wrdelmanto.papps.utils.startRotatingAnimation

@SuppressLint("SetJavaScriptEnabled")
class NasaPictureOfTheDayFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentNasaPictureOfTheDayBinding

    private val nasaPictureOfTheDayViewModel: NasaPictureOfTheDayViewModel by viewModels()

    private val androidDownloader = AndroidDownloader(context)

    private lateinit var picture: ImageView
    private lateinit var video: WebView
    private lateinit var contentLoading: ProgressBar
    private lateinit var downloadButton: ConstraintLayout
    private lateinit var downloadIcon: ImageView
    private lateinit var downloadIconDone: ImageView
    private lateinit var automaticDownloadSwitch: SwitchCompat

    private lateinit var loadingGroup: Group
    private lateinit var loadedGroup: Group

    private lateinit var internetError: ImageView
    private lateinit var contentInternetError: ImageView

    var contentLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNasaPictureOfTheDayBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nasaPictureOfTheDayViewModel = nasaPictureOfTheDayViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        picture = binding.nasaPictureOfTheDayPicture
        video = binding.nasaPictureOfTheDayVideo
        contentLoading = binding.nasaPictureOfTheDayContentLoading
        downloadButton = binding.nasaPictureOfTheDayPictureDownloadContainer
        downloadIcon = binding.nasaPictureOfTheDayPictureDownload
        downloadIconDone = binding.nasaPictureOfTheDayPictureDownloadDone
        automaticDownloadSwitch = binding.nasaPictureOfTheDayPictureAutomaticDownloadSwitch

        loadingGroup = binding.nasaPictureOfTheDayGroupLoading
        loadedGroup = binding.nasaPictureOfTheDayGroupLoaded

        internetError = binding.nasaPictureOfTheDayInternetError
        contentInternetError = binding.nasaPictureOfTheDayContentInternetError

        initiateListeners()
        initiateDownloadObservers()
        initiateViewModelObservers()
    }

    override fun onResume() {
        super.onResume()

        nasaPictureOfTheDayViewModel.resetUi(context)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initiateViewModelObservers() {
        nasaPictureOfTheDayViewModel.state.observe(viewLifecycleOwner) {
            changeViewState(it.nasaPictureOfTheDayState)
        }
        nasaPictureOfTheDayViewModel.automaticDownload.observe(viewLifecycleOwner) {
            if (it == true && contentLoaded) {
                if (nasaPictureOfTheDayViewModel.mediaType.value == "image") downloadPicture()
                else showNormalToast(
                    context, context.resources.getString(R.string.downloading_video)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeViewState(viewState: NasaPictureOfTheDayState) {
        when (viewState) {
            NasaPictureOfTheDayState.LOADING -> {
                contentLoaded = false
                nasaPictureOfTheDayViewModel.updateShouldDownloadPicture(false)
                internetError.visibility = GONE
                loadingGroup.visibility = VISIBLE
                loadedGroup.visibility = GONE
                downloadButton.visibility = GONE
            }

            NasaPictureOfTheDayState.LOADED -> {
                nasaPictureOfTheDayViewModel.updateShouldDownloadPicture(true)
                internetError.visibility = GONE
                loadingGroup.visibility = GONE
                loadedGroup.visibility = VISIBLE
                if (nasaPictureOfTheDayViewModel.mediaType.value == "image") {
                    picture.visibility = VISIBLE
                    video.visibility = GONE
                    displayPicture()
                } else {
                    picture.visibility = GONE
                    video.visibility = VISIBLE
                    displayVideo()
                }
            }

            NasaPictureOfTheDayState.ERROR -> {
                internetError.visibility = VISIBLE
                loadingGroup.visibility = GONE
                video.visibility = GONE
                picture.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.general_gray_background, null)
                if (checkInternetConnection(context)) showNormalToast(
                    context, context.resources.getString(R.string.unexpected_error)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initiateListeners() {
        picture.setOnClickListener { openFullScreen() }
        downloadButton.setOnClickListener { downloadPicture() }
        automaticDownloadSwitch.setOnCheckedChangeListener { _, isChecked ->
            nasaPictureOfTheDayViewModel.updateAutomaticDownload(isChecked)
            putSharedPreferences(
                context, SP_NPOTD_AUTOMATIC_DOWNLOAD, automaticDownloadSwitch.isChecked
            )
        }
        internetError.setOnClickListener { nasaPictureOfTheDayViewModel.getNasaData() }
        contentInternetError.setOnClickListener {
            if (nasaPictureOfTheDayViewModel.mediaType.value == "image") displayPicture()
            else displayVideo()
        }
    }

    private fun initiateDownloadObservers() {
        androidDownloader.state.observe(viewLifecycleOwner) {
            changeDownloadState(it.downloadState)
        }
    }

    private fun changeDownloadState(downloadState: DownloadState) {
        when (downloadState) {
            DownloadState.DOWNLOADING -> {
                downloadIconDone.visibility = GONE
                downloadIcon.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_rotate_right, null)

                startRotatingAnimation(downloadIcon)

                logD {
                    String.format(
                        context.resources.getString(R.string.image_downloading),
                        nasaPictureOfTheDayViewModel.title.value
                    )
                }
                showNormalToast(
                    context, String.format(
                        context.resources.getString(R.string.image_downloading),
                        nasaPictureOfTheDayViewModel.title.value
                    )
                )
            }

            DownloadState.DOWNLOADED -> {
                downloadIcon.visibility = GONE
                downloadIconDone.visibility = VISIBLE

                logD {
                    String.format(
                        context.resources.getString(R.string.image_downloaded),
                        nasaPictureOfTheDayViewModel.title.value
                    )
                }
                showNormalToast(
                    context, String.format(
                        context.resources.getString(R.string.image_downloaded),
                        nasaPictureOfTheDayViewModel.title.value
                    )
                )
            }
        }
    }

    private fun disableListeners() {
        picture.setOnClickListener(null)
        downloadButton.setOnClickListener(null)
        automaticDownloadSwitch.addTextChangedListener(null)
        internetError.setOnClickListener(null)
        contentInternetError.setOnClickListener(null)
    }

    private fun displayPicture() {
        Picasso.get().load(nasaPictureOfTheDayViewModel.url.value)
            .placeholder(R.drawable.general_gray_background)
            .into(picture, object : com.squareup.picasso.Callback {
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onSuccess() {
                    contentLoaded = true
                    contentLoading.visibility = GONE
                    contentInternetError.visibility = GONE
                    downloadButton.visibility = VISIBLE
                    if (nasaPictureOfTheDayViewModel.automaticDownload.value == true) downloadPicture()
                }

                override fun onError(e: java.lang.Exception?) {
                    contentLoading.visibility = GONE
                    downloadButton.visibility = GONE
                    contentInternetError.visibility = VISIBLE
                    if (checkInternetConnection(context)) showNormalToast(
                        context, context.resources.getString(R.string.unexpected_error)
                    )
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun downloadPicture() {
        if (nasaPictureOfTheDayViewModel.shouldDownloadPicture.value == true) {
            nasaPictureOfTheDayViewModel.updateShouldDownloadPicture(false)

            androidDownloader.run {
                updateDownloadInfo(
                    nasaPictureOfTheDayViewModel.title.value.toString()
                )
                downloadFile(nasaPictureOfTheDayViewModel.hdurl.value.toString())
            }
        }
    }

    private fun openFullScreen() {
        startActivity(
            Intent(
                context, NasaPictureOfTheDayHdurlActivity::class.java
            ).putExtra("hdurl", nasaPictureOfTheDayViewModel.hdurl.value)
        )
    }

    private fun openYoutube() {
        // TODO: ?
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun displayVideo() {
        // TODO: ?
        video.apply {
            webViewClient = WebViewClient()
            webChromeClient = CustomChromeClient()
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            loadUrl("https://www.youtube.com/embed/YE7VzlLtp-4")
        }
    }

    private inner class CustomChromeClient : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0

        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(context.resources, 2130837573)
        }

        override fun onHideCustomView() {
//            (window.decorView as FrameLayout).removeView(mCustomView)
//            mCustomView = null
//            window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
//            requestedOrientation = mOriginalOrientation
//            mCustomViewCallback!!.onCustomViewHidden()
//            mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View?, paramCustomViewCallback: CustomViewCallback?
        ) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
//            mCustomView = paramView
//            mOriginalSystemUiVisibility = window.decorView.systemUiVisibility
//            mOriginalOrientation = requestedOrientation
//            mCustomViewCallback = paramCustomViewCallback
//            (window.decorView as FrameLayout).addView(mCustomView, FrameLayout.LayoutParams(-1, -1))
//            window.decorView.systemUiVisibility = 3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}