package com.example.kaupark.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentParkingMapBinding

class ParkingMap : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentParkingMapBinding.inflate(inflater, container, false)
        binding.maptoolbar.apply {
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
        return binding.root
    }
}