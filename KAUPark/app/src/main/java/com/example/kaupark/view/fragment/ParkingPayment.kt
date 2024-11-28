package com.example.kaupark.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentParkingPaymentBinding
import com.example.kaupark.view.adapter.ImageAdapter
import com.google.firebase.firestore.FirebaseFirestore

class ParkingPayment : Fragment() {

    private lateinit var binding: FragmentParkingPaymentBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var deposit = 0
    private val userDocumentId = "PfANoIw3kBX8V2BmfgAsDz6Rf423"

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
            loadDeposit {
                processPayment()
            }
        }

        // 정기권 및 현장 요금 선택에 따른 결제 버튼 텍스트 변경
        setupRadioButtons()

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
                loadDuration { duration ->
                    binding.button2.text = "${duration}원 결제하기"
                }
            } else {
                binding.button2.text = "결제하기"
            }
        }
    }

    private fun loadDeposit(onDepositLoaded: () -> Unit) {
        firestore.collection("users")
            .document(userDocumentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.contains("deposit")) {
                    deposit = document.getLong("deposit")?.toInt() ?: 0
                    Log.d("ParkingPayment", "Current deposit: $deposit")
                    onDepositLoaded()
                } else {
                    Log.e("ParkingPayment", "No deposit field found.")
                }
            }
    }

    private fun processPayment() {
        val isRegularSelected = binding.radioRegular.isChecked
        val isOnSiteSelected = binding.radioOnSite.isChecked

        if (isRegularSelected) {
            val cost = 60000
            if (deposit >= cost) {
                val newDeposit = deposit - cost
                updateDeposit(newDeposit)
            } else {
                Toast.makeText(requireContext(), "잔액이 부족합니다.", Toast.LENGTH_SHORT).show()
            }
        } else if (isOnSiteSelected) {
            loadDuration { duration ->
                if (deposit >= duration) {
                    val newDeposit = deposit - duration
                    updateDeposit(newDeposit)
                } else {
                    Toast.makeText(requireContext(), "잔액이 부족합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "정기권 또는 현장 결제를 선택하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDeposit(newDeposit: Int) {
        firestore.collection("users")
            .document(userDocumentId)
            .update("deposit", newDeposit)
            .addOnSuccessListener {
                deposit = newDeposit
                Toast.makeText(requireContext(), "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadDuration(onDurationLoaded: (Int) -> Unit) {
        firestore.collection("users")
            .document(userDocumentId)
            .collection("parking_records")
            .document("duration")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.contains("duration")) {
                    val duration = document.getLong("duration")?.toInt() ?: 0
                    Log.d("ParkingPayment", "Duration: $duration")
                    onDurationLoaded(duration)
                }
            }
    }
}
