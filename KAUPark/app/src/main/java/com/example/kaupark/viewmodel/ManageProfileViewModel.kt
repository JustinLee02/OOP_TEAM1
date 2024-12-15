package com.example.kaupark.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.ParkingItemModel
import com.example.kaupark.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ManageProfileViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _parkingItems = MutableLiveData<List<ParkingItemModel>>()
    val parkingItems: LiveData<List<ParkingItemModel>> get() = _parkingItems

    private val _userInfo = MutableLiveData<UserModel>()
    val userInfo: LiveData<UserModel> get() = _userInfo

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun fetchUserInfo() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val name = document.getString("name")
                val studentId = document.getString("studentId")
                val email = document.getString("email")
                val phoneNum = document.getString("phoneNum")
                val carNum = document.getString("carNum")
                val userInfo = UserModel(name = name!!, studentId = studentId!!, email = email!!, phoneNum = phoneNum!!, carNum = carNum!!, password = "", deposit = 0)
                _userInfo.value = userInfo
            }
    }

    fun fetchParkingRecords() {
        val userId = auth.currentUser?.uid ?: return // 현재 로그인한 사용자의 ID 가져오기

        firestore.collection("users")
            .document(userId)
            .collection("parking_records")
            .get()
            .addOnSuccessListener { documents ->
                val parkingList = mutableListOf<ParkingItemModel>()
                // Firestore에서 데이터를 가져와 parkingItems에 추가
                for (document in documents) {
                    val date = document.getString("date") ?: ""
                    val durationMillis = document.getLong("duration") ?: 0L
                    Log.d("Firestore", "$durationMillis")
                    val durationSecs = "${durationMillis / 1000} sec"
                    val fee = "${durationMillis /1000 * 100}원"

                    parkingList.add(ParkingItemModel(date, fee, durationSecs))
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