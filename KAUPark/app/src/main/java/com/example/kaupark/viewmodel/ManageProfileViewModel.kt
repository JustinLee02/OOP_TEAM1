package com.example.kaupark.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.ParkingItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ManageProfileViewModel(): ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _parkingItems = MutableLiveData<List<ParkingItem>>()
    val parkingItems: LiveData<List<ParkingItem>> get() = _parkingItems

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun fetchParkingRecords() {
        val userId = auth.currentUser?.uid ?: return // 현재 로그인한 사용자의 ID 가져오기

        firestore.collection("users")
            .document(userId)
            .collection("parking_records")
            .get()
            .addOnSuccessListener { documents ->
                val parkingList = mutableListOf<ParkingItem>()
                // Firestore에서 데이터를 가져와 parkingItems에 추가
                for (document in documents) {
                    val date = document.getString("date") ?: ""
                    val durationMillis = document.getLong("duration") ?: 0L
                    Log.d("Firestore", "$durationMillis")
                    val durationSecs = "${durationMillis / 1000} sec"
                    val fee = "${durationMillis /1000 * 100}원"

                    parkingList.add(ParkingItem(date, fee, durationSecs))
                }
                _parkingItems.value = parkingList
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching parking records: ${e.message}")
            }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

}