package com.example.kaupark.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kaupark.ParkingRecordAdapter
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentManageProfileBinding
import com.example.kaupark.model.ParkingItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ManageProfile : Fragment() {

    private lateinit var binding: FragmentManageProfileBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val parkingItems = mutableListOf<ParkingItem>()

//    private val parkingItems = listOf(
//        ParkingItem("2024-11-16", "12000", "3h 13min"),
//        ParkingItem("2024-11-15", "8000", "2h 5min"),
//        ParkingItem("2024-11-14", "10000", "2h 45min")
//    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageProfileBinding.inflate(inflater, container, false)
        binding.recyclerviewParkingrecord.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ParkingRecordAdapter(parkingItems)
        }

        fetchParkingRecords()

        return binding.root
    }

    private fun fetchParkingRecords() {
        val userId = auth.currentUser?.uid ?: return // 현재 로그인한 사용자의 ID 가져오기

        firestore.collection("users")
            .document(userId)
            .collection("parking_records")
            .get()
            .addOnSuccessListener { documents ->
                parkingItems.clear()
                // Firestore에서 데이터를 가져와 parkingItems에 추가
                for (document in documents) {
                    val date = document.getString("date") ?: ""
                    val durationMillis = document.getLong("duration") ?: 0L
                    Log.d("Firestore", "$durationMillis")
                    val durationSecs = "${durationMillis / 1000} sec"
                    val fee = "${durationMillis /1000 * 100}원"



                    parkingItems.add(ParkingItem(date, fee, durationSecs))
                }

                // RecyclerView 갱신
                binding.recyclerviewParkingrecord.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching parking records: ${e.message}")
            }
    }

}