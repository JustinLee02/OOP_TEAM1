package com.example.kaupark.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.ParkingRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Encapsulation
    // The value can only be modified within the ViewModel
    private val _userCarNum = MutableLiveData<String>()
    val userCarNum: LiveData<String> get() = _userCarNum

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _parkingRecord = MutableLiveData<ParkingRecord?>()
    val parkingRecord: LiveData<ParkingRecord?> get() = _parkingRecord

    private val _parkingFee = MutableLiveData<String>()
    val parkingFee: LiveData<String> get() = _parkingFee


    // Fetching user info
    // User car number, id
    fun fetchUserInfo() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if(document != null) {
                    val carNum = document.getString("carNum") ?: "차량 번호 없음"
                    val name = document.getString("id") ?: "이름 없음"

                    _userCarNum.value = carNum
                    _userName.value = name
                } else {
                    // Toast.makeText(this, "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Toast.makeText(this, "실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    // Record user entry time to millis
    fun recordEntryTime() {
        val userId = auth.currentUser?.uid ?: return
        val entryTime = System.currentTimeMillis()

        val record = ParkingRecord(entryTime = entryTime)

        firestore.collection("users").document(userId)
            .collection("parking_records").document("duration")
            .set(record)
            .addOnSuccessListener {
                _parkingRecord.value = record
            }
            .addOnFailureListener {
                _parkingRecord.value = null
            }
    }

    // Record user exit time to millis
    // Calculate duration by exitTime - entryTime
    fun recordExitTime() {
        val userId = auth.currentUser?.uid ?: return
        val exitTime = System.currentTimeMillis()

        firestore.collection("users").document(userId)
            .collection("parking_records").document("duration")
            .get()
            .addOnSuccessListener { document ->
                val entryTime = document.getLong("entryTime") ?: return@addOnSuccessListener
                val duration = exitTime - entryTime

                val updatedRecord = ParkingRecord(entryTime, exitTime, duration)
                firestore.collection("users").document(userId)
                    .collection("parking_records").document("duration")
                    .set(updatedRecord)
                    .addOnSuccessListener {
                        _parkingRecord.value = updatedRecord
                        calculateParkingFee(duration)
                    }
            }
            .addOnFailureListener {
                _parkingRecord.value = null
            }
    }

    // Calculating Parking Fee
    private fun calculateParkingFee(durationMillis: Long) {
        val durationSecs = durationMillis / 1000
        val durationMins = durationSecs / 60

        _parkingFee.value = when {
            durationMins < 30 -> "30분 무료 ${durationMins}분 주차중"
            durationMins < 60 -> "2000원 ${durationMins}분 주차중"
            else -> {
                val additionalFee = ((durationMins - 60) / 30) * 500
                "${2000 + additionalFee}원 ${durationMins}분 주차중"
            }
        }
    }

}