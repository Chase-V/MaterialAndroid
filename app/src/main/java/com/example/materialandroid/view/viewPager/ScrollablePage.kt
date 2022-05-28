package com.example.materialandroid.view.viewPager

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
            val dayBefore = calendar.add(Calendar.DATE, -getInt(ARG_OBJECT))
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
                Toast.makeText(context, "Нет ответа от сервера :(", Toast.LENGTH_LONG).show()
                binding.scrollablePageImageView.load(R.drawable.net_interneta)
            }
            is POTDState.Loading -> {
                binding.scrollablePageImageView.load(R.drawable.nasa_logo)
            }
            is POTDState.Success -> {
                val pictureOfTheDayResponseData = state.pictureOfTheDayResponseData
                val url = pictureOfTheDayResponseData.url
                binding.scrollablePageImageView.load(url) {
                    lifecycle(this@ScrollablePage)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                }
                binding.scrollablePageTitle.text =
                    pictureOfTheDayResponseData.title
                binding.scrollablePageDescription.text =
                    pictureOfTheDayResponseData.explanation
            }
        }
    }
}