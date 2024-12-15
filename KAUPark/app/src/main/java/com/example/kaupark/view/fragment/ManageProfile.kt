package com.example.kaupark.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaupark.ParkingRecordAdapter
import com.example.kaupark.ToastHelper
import com.example.kaupark.databinding.FragmentManageProfileBinding
import com.example.kaupark.viewmodel.ManageProfileViewModel

class ManageProfile : Fragment() {

    private lateinit var binding: FragmentManageProfileBinding

    private val viewModel: ManageProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageProfileBinding.inflate(inflater, container, false)

        binding.recyclerviewParkingrecord.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ParkingRecordAdapter(emptyList()) // 초기에는 빈 리스트
        }

        viewModel.parkingItems.observe(viewLifecycleOwner) { parkingItems ->
            (binding.recyclerviewParkingrecord.adapter as ParkingRecordAdapter).apply {
                this.parkingItems = parkingItems // RecyclerView 데이터 갱신
                notifyDataSetChanged()
            }
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                ToastHelper.showToast(requireContext(), it)
                viewModel.clearToastMessage()
            }
        }

        viewModel.fetchParkingRecords()

        return binding.root
    }
}