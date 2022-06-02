package com.example.materialandroid.view.viewPager

import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.materialandroid.R
import com.example.materialandroid.databinding.FragmentForViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class FragmentForViewPager : Fragment(R.layout.fragment_for_view_pager) {


    companion object {
        fun newInstance(): FragmentForViewPager {
            return FragmentForViewPager()
        }
    }

    private lateinit var fragmentForViewPagerAdapter:FragmentForViewPagerAdapter
    private lateinit var viewPager:ViewPager2

    private var _binding: FragmentForViewPagerBinding? = null

    private val binding: FragmentForViewPagerBinding
        get() {
            return _binding!!
        }

    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentForViewPagerAdapter = FragmentForViewPagerAdapter(this)
        viewPager = binding.viewPager

        viewPager.adapter = fragmentForViewPagerAdapter

        val tabLayout = binding.tabs

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = getDateForTabTitle(position)
        }.attach()
    }

    private fun getDateForTabTitle(position: Int):String{
        val calendar: Calendar = Calendar.getInstance()
        val dayBefore = calendar.add(Calendar.DATE, -position)
        return simpleDateFormat.format(calendar.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

const val ARG_OBJECT = "object"

class FragmentForViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 30

    override fun createFragment(position: Int): Fragment {
        val fragment = ScrollablePage.newInstance()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
        }
        return fragment
    }

}