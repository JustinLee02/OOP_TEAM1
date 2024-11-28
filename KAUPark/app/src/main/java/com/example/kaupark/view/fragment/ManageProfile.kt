package com.example.kaupark.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kaupark.ParkingRecordAdapter
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentManageProfileBinding
import com.example.kaupark.model.ParkingItem

class ManageProfile : Fragment() {

    private val parkingItems = listOf(
        ParkingItem("2024-11-16", "12000", "3h 13min"),
        ParkingItem("2024-11-15", "8000", "2h 5min"),
        ParkingItem("2024-11-14", "10000", "2h 45min")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentManageProfileBinding.inflate(inflater, container, false)
        binding.recyclerviewParkingrecord.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ParkingRecordAdapter(parkingItems)
        }

        return binding.root
    }

}