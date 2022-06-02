package com.example.materialandroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.materialandroid.R
import com.example.materialandroid.databinding.BottomNavigationLayoutBinding
import com.example.materialandroid.view.viewPager.FragmentForViewPager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding: BottomNavigationLayoutBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navitionMenuOne -> {

                    requireActivity().supportFragmentManager.commit{
                        setReorderingAllowed(true)
                        replace(R.id.container, FragmentForViewPager.newInstance())
                        addToBackStack("ViewPagerFragment")
                    }

                }

                R.id.navitionMenuTwo -> {
                    Toast.makeText(context, "Two", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

}