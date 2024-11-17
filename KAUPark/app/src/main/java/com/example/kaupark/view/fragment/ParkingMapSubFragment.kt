package com.example.kaupark.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentParkingMapSubBinding

class ParkingMapSubFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentParkingMapSubBinding.inflate(inflater, container, false)

        val tagValue = arguments?.getString("captiontext") ?: "정보 없음"
        binding.parkinglotname.text = tagValue

        return binding.root
    }

    companion object {
        fun newInstance(captiontext: String): ParkingMapSubFragment {

            val fragment = ParkingMapSubFragment()
            val args = Bundle()
            args.putString("captiontext", captiontext)
            fragment.arguments = args
            return fragment
        }
    }
}