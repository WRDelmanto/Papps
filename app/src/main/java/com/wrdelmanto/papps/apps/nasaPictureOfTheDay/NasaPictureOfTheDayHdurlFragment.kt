package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentNasaPictureOfTheDayHdurlBinding
import com.wrdelmanto.papps.utils.showNormalToast

class NasaPictureOfTheDayHdurlFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentNasaPictureOfTheDayHdurlBinding

    private lateinit var nasaPictureOfTheDayHdurlActivity: NasaPictureOfTheDayHdurlActivity

    private lateinit var picture: ImageView
    private lateinit var pictureLoading: ProgressBar

    private lateinit var pictureInternetError: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNasaPictureOfTheDayHdurlBinding.inflate(layoutInflater)

        nasaPictureOfTheDayHdurlActivity = activity as NasaPictureOfTheDayHdurlActivity

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        picture = binding.nasaPictureOfTheDayHdurlPicture
        pictureLoading = binding.nasaPictureOfTheDayHdurlLoading

        pictureInternetError = binding.nasaPictureOfTheDayHdurlPictureInternetError

        displayPicture()
        initiateListeners()
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateListeners() = pictureInternetError.setOnClickListener { displayPicture() }

    private fun disableListeners() = pictureInternetError.setOnClickListener(null)

    private fun displayPicture() {
        pictureLoading.visibility = VISIBLE
        pictureInternetError.visibility = GONE

        Picasso.get().load(nasaPictureOfTheDayHdurlActivity.hdurl)
            .into(picture, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    pictureLoading.visibility = GONE
                    pictureInternetError.visibility = GONE
                }

                override fun onError(e: java.lang.Exception?) {
                    pictureLoading.visibility = GONE
                    pictureInternetError.visibility = VISIBLE
                    showNormalToast(
                        context,
                        context.resources.getString(R.string.no_internet_connection_warning)
                    )
                }
            })
    }
}