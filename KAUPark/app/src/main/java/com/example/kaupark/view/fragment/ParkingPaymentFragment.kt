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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ParkingPaymentFragment : Fragment() {

    private lateinit var binding: FragmentParkingPaymentBinding
    private val parkingPaymentViewModel: ParkingPaymentViewModel by activityViewModels()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingPaymentBinding.inflate(inflater, container, false)

        // 이미지 리스트 생성
        val imageList = listOf(
            R.drawable.image_card1, R.drawable.image_card2, R.drawable.image_card3,
            R.drawable.image_card4, R.drawable.image_card5, R.drawable.image_card6,
            R.drawable.image_card7, R.drawable.image_card8, R.drawable.image_card9,
        )

        // RecyclerView 설정
        setupRecyclerView(imageList)

        // 결제 버튼 로직
        binding.button2.setOnClickListener {
            val isRegularSelected = binding.radioRegular.isChecked
            val isOnSiteSelected = binding.radioOnSite.isChecked

            if (isOnSiteSelected) {
                loadLatestDuration { duration ->
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
                loadLatestDuration { duration ->
                    binding.button2.text = "${duration}원 결제하기"
                }
            } else {
                binding.button2.text = "결제하기"
            }
        }
    }

    // 최신 duration 값을 Firebase에서 가져오는 함수
    private fun loadLatestDuration(callback: (Int) -> Unit) {
        // Firestore에서 데이터를 가져오기
        val userId = auth?.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .collection("parking_records")
            .orderBy("entryTime", Query.Direction.DESCENDING)  // 가장 최신 문서부터 정렬
            .limit(1)  // 가장 최신 한 문서만 가져오기
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val latestDocument = documents.documents[0]
                    val duration = latestDocument.getLong("duration")?.toInt() ?: 0
                    val durationSecs = duration / 1000
                    callback(durationSecs * 100)  // 최신 duration 값을 callback으로 전달
                } else {
                    callback(0)  // 문서가 없다면 기본값 0
                }
            }
            .addOnFailureListener { exception ->
                // 실패 처리
                Toast.makeText(requireContext(), "Error loading duration: ${exception.message}", Toast.LENGTH_SHORT).show()
                callback(0)  // 실패 시 기본값 0
            }
    }
}
