package com.example.materialandroid.view.viewPager

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.materialandroid.R
import com.example.materialandroid.databinding.ScrollablePageBinding
import com.example.materialandroid.viewModel.POTDState
import com.example.materialandroid.viewModel.POTDViewModel
import com.stfalcon.imageviewer.StfalconImageViewer
import java.text.SimpleDateFormat
import java.util.*

class ScrollablePage : Fragment(R.layout.scrollable_page) {

    companion object {
        fun newInstance(): ScrollablePage {
            return ScrollablePage()
        }
    }

    private val viewModel: POTDViewModel by lazy {
        ViewModelProvider(this).get(POTDViewModel::class.java)
    }

    private var _binding: ScrollablePageBinding? = null

    private val binding: ScrollablePageBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScrollablePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()


        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            calendar.add(Calendar.DATE, -getInt(ARG_OBJECT))
            viewModel.sendServerRequest(simpleDateFormat.format(calendar.time))

        }

        viewModel.getData().observe(viewLifecycleOwner) {
            renderData(it)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderData(state: POTDState) {
        when (state) {
            is POTDState.Error -> {
                Toast.makeText(context, "?????? ???????????? ???? ?????????????? :(", Toast.LENGTH_LONG).show()
                binding.scrollablePageImageView.load(R.drawable.net_interneta) { crossfade(700) }
            }
            is POTDState.Loading -> {
                binding.scrollablePageImageView.load(R.drawable.nasa_logo) { crossfade(700) }
            }
            is POTDState.Success -> {
                val pictureOfTheDayResponseData = state.pictureOfTheDayResponseData

                if (pictureOfTheDayResponseData.mediaType.equals("video", true)) {

                    if (pictureOfTheDayResponseData.thumbnailUrl.isNullOrEmpty()) {
                        binding.scrollablePageImageView.load(R.drawable.ic_baseline_play_circle_filled_24) {
                            crossfade(
                                700
                            )
                        }
                    } else {
                        binding.scrollablePageImageView.load(pictureOfTheDayResponseData.thumbnailUrl) {
                            crossfade(700)
                            lifecycle(this@ScrollablePage)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                        }
                    }

                    binding.scrollableCollapseHint.text = getString(R.string.open_video_hint)
                    binding.scrollablePageTitle.text =
                        pictureOfTheDayResponseData.title
                    binding.scrollablePageDescription.text =
                        pictureOfTheDayResponseData.explanation

                    binding.scrollablePageImageView.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(pictureOfTheDayResponseData.url)
                        })
                    }

                } else {
                    val url = pictureOfTheDayResponseData.url
                    binding.scrollablePageImageView.load(url) {
                        crossfade(700)
                        lifecycle(this@ScrollablePage)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)

                    }

                    binding.scrollablePageImageView.setOnClickListener {
                        val images =
                            mutableListOf<Drawable>(binding.scrollablePageImageView.drawable)

                        StfalconImageViewer.Builder(context, images) { view, _ ->
                            view.load(url)
                        }.withTransitionFrom(binding.scrollablePageImageView).show()
                    }
                    binding.scrollablePageTitle.text =
                        pictureOfTheDayResponseData.title
                    binding.scrollablePageDescription.text =
                        pictureOfTheDayResponseData.explanation
                }
            }
        }
    }

}