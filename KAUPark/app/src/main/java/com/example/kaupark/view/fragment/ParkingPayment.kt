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

        val imageSlider = binding.root.findViewById<ImageSlider>(R.id.image_slider)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.image1, "카드1", ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.image2, "카드2", ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.image3, "카드3", ScaleTypes.FIT))

        imageSlider.setImageList(imageList)

        // 결제하기 버튼의 id : button2
        binding.button2.setOnClickListener {
            loadDeposit {
                processPayment()
            }
        }

        // 정기권 결제 버튼의 id : radioRegular
        binding.radioRegular.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                loadDuration { duration ->
                    binding.button2.text = "60000원 결제하기"
                }
            } else {
                binding.button2.text = "결제하기"
            }
        }

        // 현장 요금 결제 버튼의 id : radioOnSite
        binding.radioOnSite.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                loadDuration { duration ->
                    binding.button2.text = "${duration}원 결제하기"
                }
            } else {
                binding.button2.text = "결제하기"
            }
        }

        return binding.root
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

        // 정기권 결제가 선택되어야됨
        if (isRegularSelected) {
            val cost = 60000
            if (deposit >= cost) {
                val newDeposit = deposit - cost
                updateDeposit(newDeposit)
            } else {
                Toast.makeText(requireContext(), "잔액이 부족합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 현장결제가 선택되어야됨
        else if (isOnSiteSelected) {
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
