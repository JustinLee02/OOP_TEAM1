package com.example.kaupark.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentParkingPaymentBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class ParkingPayment : Fragment() {

    private lateinit var binding: FragmentParkingPaymentBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var deposit = 0
    private val userDocumentId = "PfANoIw3kBX8V2BmfgAsDz6Rf423" // 사용자 문서 ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingPaymentBinding.inflate(inflater, container, false)

        // ImageSlider 초기화
        val imageSlider = binding.root.findViewById<ImageSlider>(R.id.image_slider)
        val imageList = ArrayList<SlideModel>()

        // 이미지 추가 (로컬 리소스 또는 URL 사용 가능)
        imageList.add(SlideModel(R.drawable.image1, "카드1", ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.image2, "카드2", ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.image3, "카드3", ScaleTypes.FIT))

        // 슬라이더에 이미지 리스트 설정
        imageSlider.setImageList(imageList)

        // 결제 버튼 클릭 이벤트
        binding.button2.setOnClickListener {
            loadDeposit {
                processPayment()
            }
        }

        // 정기권 결제 RadioButton 클릭 이벤트
        binding.radioRegular.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 정기권 결제 선택 시 60,000원 표시
                binding.button2.text = "60,000원 결제하기"
            } else {
                // 선택하지 않으면 공백으로 처리
                binding.button2.text = "결제하기"
            }
        }

        return binding.root
    }

    private fun loadDeposit(onDepositLoaded: () -> Unit) {
        firestore.collection("users") // Firebase에 있는 컬렉션 이름 (예: "users")
            .document(userDocumentId) // 특정 사용자 문서 ID
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.contains("deposit")) {
                    deposit = document.getLong("deposit")?.toInt() ?: 0
                    Log.d("ParkingPayment", "Current deposit: $deposit")
                    onDepositLoaded()  // deposit이 로드된 후 결제 처리
                } else {
                    Log.e("ParkingPayment", "No deposit field found.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ParkingPayment", "Error getting deposit: ", exception)
            }
    }

    private fun processPayment() {
        // 정기권 체크박스 상태 확인
        val isRegularSelected = binding.radioRegular.isChecked // binding을 사용하여 RadioButton 찾기

        if (isRegularSelected) {
            val cost = 60000 // 정기권 결제 금액
            if (deposit >= cost) {
                // 잔액 차감 후 업데이트
                val newDeposit = deposit - cost
                updateDeposit(newDeposit)
            } else {
                Toast.makeText(requireContext(), "잔액이 부족합니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "정기권 결제를 선택하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDeposit(newDeposit: Int) {
        firestore.collection("users")
            .document(userDocumentId) // 특정 사용자 문서 ID
            .update("deposit", newDeposit)
            .addOnSuccessListener {
                deposit = newDeposit // 로컬 변수 업데이트
                Toast.makeText(requireContext(), "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.e("ParkingPayment", "Error updating deposit: ", exception)
                Toast.makeText(requireContext(), "결제 처리 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
    }
}
