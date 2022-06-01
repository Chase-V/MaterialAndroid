package com.example.materialandroid.view.animations

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.materialandroid.R
import com.example.materialandroid.databinding.AnimationsFragmentBinding
import com.example.materialandroid.viewModel.POTDState
import com.example.materialandroid.viewModel.POTDViewModel

class AnimationsFragment : Fragment(R.layout.animations_fragment) {

    companion object {
        fun newInstance(): AnimationsFragment {
            return AnimationsFragment()
        }
    }

    private val viewModel: POTDViewModel by lazy {
        ViewModelProvider(this).get(POTDViewModel::class.java)
    }

    private var _binding: AnimationsFragmentBinding? = null

    private val binding: AnimationsFragmentBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AnimationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar: Toolbar = binding.toolbar
        actionBar.inflateMenu(R.menu.animation_menu)
        actionBar.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.app_bar_search -> Toast.makeText(
                    requireContext(),
                    "Search",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.app_bar_second_button -> Toast.makeText(
                    requireContext(),
                    "Dummy button",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

        viewModel.getData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        viewModel.sendServerRequest()
    }

    private fun renderData(state: POTDState) {
        when (state) {
            is POTDState.Error -> {
                Toast.makeText(context, "Нет ответа от сервера :(", Toast.LENGTH_LONG).show()
                binding.appBarImage.load(R.drawable.net_interneta)
            }
            is POTDState.Loading -> {
                binding.appBarImage.load(R.drawable.nasa_logo)
            }
            is POTDState.Success -> {
                val pictureOfTheDayResponseData = state.pictureOfTheDayResponseData

                if (pictureOfTheDayResponseData.mediaType.equals("video", true)) {

                    if (pictureOfTheDayResponseData.thumbnailUrl.isNullOrEmpty()) {
                        binding.appBarImage.load(R.drawable.ic_baseline_play_circle_filled_24)
                    } else {
                        binding.appBarImage.load(pictureOfTheDayResponseData.thumbnailUrl) {
                            lifecycle(this@AnimationsFragment)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                        }
                    }

                    binding.collapseHeader.text =
                        pictureOfTheDayResponseData.title
                    binding.collapseDesc.text =
                        pictureOfTheDayResponseData.explanation

                    binding.appBarImage.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(pictureOfTheDayResponseData.url)
                        })
                    }

                } else {
                    val url = pictureOfTheDayResponseData.url
                    binding.appBarImage.load(url) {
                        lifecycle(this@AnimationsFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    binding.collapseHeader.text =
                        pictureOfTheDayResponseData.title
                    binding.collapseDesc.text =
                        pictureOfTheDayResponseData.explanation
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}