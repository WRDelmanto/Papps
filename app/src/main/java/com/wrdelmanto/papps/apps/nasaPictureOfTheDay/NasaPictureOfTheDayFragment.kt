package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
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
import com.wrdelmanto.papps.utils.downloadExtension.AndroidDownloader
import com.wrdelmanto.papps.utils.downloadExtension.DownloadState
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.showNormalToast
import com.wrdelmanto.papps.utils.startRotatingAnimation

class NasaPictureOfTheDayFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentNasaPictureOfTheDayBinding

    private val nasaPictureOfTheDayViewModel: NasaPictureOfTheDayViewModel by viewModels()

    private val androidDownloader = AndroidDownloader(context)

    private lateinit var picture: ImageView
    private lateinit var pictureLoading: ProgressBar
    private lateinit var downloadButton: ConstraintLayout
    private lateinit var downloadIcon: ImageView
    private lateinit var downloadIconDone: ImageView
    private lateinit var automaticDownloadSwitch: SwitchCompat

    private lateinit var loadingGroup: Group
    private lateinit var loadedGroup: Group

    private lateinit var internetError: ImageView
    private lateinit var pictureInternetError: ImageView

    var pictureLoaded = false

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
        pictureLoading = binding.nasaPictureOfTheDayPictureLoading
        downloadButton = binding.nasaPictureOfTheDayPictureDownloadContainer
        downloadIcon = binding.nasaPictureOfTheDayPictureDownload
        downloadIconDone = binding.nasaPictureOfTheDayPictureDownloadDone
        automaticDownloadSwitch = binding.nasaPictureOfTheDayPictureAutomaticDownloadSwitch

        loadingGroup = binding.nasaPictureOfTheDayGroupLoading
        loadedGroup = binding.nasaPictureOfTheDayGroupLoaded

        internetError = binding.nasaPictureOfTheDayInternetError
        pictureInternetError = binding.nasaPictureOfTheDayPictureInternetError

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
            if (it == true && pictureLoaded) downloadPicture()
        }
    }

    private fun changeViewState(viewState: NasaPictureOfTheDayState) {
        when (viewState) {
            NasaPictureOfTheDayState.LOADING -> {
                pictureLoaded = false
                nasaPictureOfTheDayViewModel.shouldDownloadPicture.value = false
                internetError.visibility = GONE
                loadingGroup.visibility = VISIBLE
                loadedGroup.visibility = GONE
                downloadButton.visibility = GONE
            }

            NasaPictureOfTheDayState.LOADED -> {
                nasaPictureOfTheDayViewModel.shouldDownloadPicture.value = true
                internetError.visibility = GONE
                loadingGroup.visibility = GONE
                loadedGroup.visibility = VISIBLE
                displayPicture()
            }

            NasaPictureOfTheDayState.ERROR -> {
                internetError.visibility = VISIBLE
                loadingGroup.visibility = GONE
                picture.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.general_gray_background, null)
                showNormalToast(
                    context, context.resources.getString(R.string.no_internet_connection_warning)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initiateListeners() {
        picture.setOnClickListener { openFullScreen() }
        downloadButton.setOnClickListener { downloadPicture() }
        automaticDownloadSwitch.setOnCheckedChangeListener { _, isChecked ->
            nasaPictureOfTheDayViewModel.automaticDownload.postValue(isChecked)
            putSharedPreferences(
                context, SP_NPOTD_AUTOMATIC_DOWNLOAD, automaticDownloadSwitch.isChecked
            )
        }
        internetError.setOnClickListener { nasaPictureOfTheDayViewModel.getNasaData() }
        pictureInternetError.setOnClickListener { displayPicture() }
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
        pictureInternetError.setOnClickListener(null)
    }

    private fun displayPicture() {
        Picasso.get().load(nasaPictureOfTheDayViewModel.url.value)
            .placeholder(R.drawable.general_gray_background)
            .into(picture, object : com.squareup.picasso.Callback {
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onSuccess() {
                    pictureLoaded = true
                    pictureLoading.visibility = GONE
                    pictureInternetError.visibility = GONE
                    downloadButton.visibility = VISIBLE
                    if (nasaPictureOfTheDayViewModel.automaticDownload.value == true) downloadPicture()
                }

                override fun onError(e: java.lang.Exception?) {
                    pictureLoading.visibility = GONE
                    downloadButton.visibility = GONE
                    pictureInternetError.visibility = VISIBLE
                    showNormalToast(
                        context,
                        context.resources.getString(R.string.no_internet_connection_warning)
                    )
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun downloadPicture() {
        if (nasaPictureOfTheDayViewModel.shouldDownloadPicture.value == true) {
            nasaPictureOfTheDayViewModel.shouldDownloadPicture.value = false

            androidDownloader.run {
                updateDownloadInfo(
                    nasaPictureOfTheDayViewModel.title.value.toString(),
                    nasaPictureOfTheDayViewModel.description.value.toString()
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
}