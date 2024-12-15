package com.example.kaupark.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentParkingPaymentBinding
import com.example.kaupark.view.adapter.ImageAdapter
import com.example.kaupark.viewmodel.ParkingPaymentViewModel

class ParkingPayment : Fragment() {

    private lateinit var binding: FragmentParkingPaymentBinding
    private val parkingPaymentViewModel: ParkingPaymentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingPaymentBinding.inflate(inflater, container, false)

        // 이미지 리스트 생성
        val imageList = listOf(
            R.drawable.image1, R.drawable.image2, R.drawable.image3,
            R.drawable.image4, R.drawable.image5, R.drawable.image6,
            R.drawable.image7, R.drawable.image8, R.drawable.image9,
        )

        // RecyclerView 설정
        setupRecyclerView(imageList)

        // 결제 버튼 로직
        binding.button2.setOnClickListener {
            val isRegularSelected = binding.radioRegular.isChecked
            val isOnSiteSelected = binding.radioOnSite.isChecked

            if (isOnSiteSelected) {
                parkingPaymentViewModel.loadDuration { duration ->
                    parkingPaymentViewModel.processPayment(isRegularSelected, isOnSiteSelected, duration)
                }
            } else {
                parkingPaymentViewModel.processPayment(isRegularSelected, isOnSiteSelected, null)
            }
        }

        // 정기권 및 현장 요금 선택에 따른 결제 버튼 텍스트 변경
        setupRadioButtons()

        // 결제 결과를 UI에 반영
        parkingPaymentViewModel.paymentResult.observe(viewLifecycleOwner) { result ->
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setupRecyclerView(imageList: List<Int>) {
        val recyclerView = binding.imageRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = ImageAdapter(requireContext(), imageList)
    }

    private fun setupRadioButtons() {
        // 정기권 결제 버튼
        binding.radioRegular.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.button2.text = "60000원 결제하기"
            } else {
                binding.button2.text = "결제하기"
            }
        }

        // 현장 요금 결제 버튼
        binding.radioOnSite.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                parkingPaymentViewModel.loadDuration { duration ->
                    binding.button2.text = "${duration}원 결제하기"
                }
            } else {
                binding.button2.text = "결제하기"
            }
        }
    }
}
