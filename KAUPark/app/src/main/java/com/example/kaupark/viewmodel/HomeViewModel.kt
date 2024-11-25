package com.example.kaupark.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.data.MyApp
import com.example.kaupark.model.ParkingRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel() : ViewModel() {

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

    private val _parkingSpace = MutableLiveData<Int>()
    val parkingSpace: LiveData<Int> get() = _parkingSpace

    private val _isEntry = MutableLiveData<Boolean>()
    val isEntry: LiveData<Boolean> get() = _isEntry

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage


    init {
        _isEntry.value = false
    }

    // Fetching user info
    // User car number, id
    fun fetchUserInfo() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val carNum = document.getString("carNum") ?: "차량 번호 없음"
                    val name = document.getString("name") ?: "이름 없음"

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
        if (_isEntry.value == true) {
            _toastMessage.value = "출차 버튼을 눌러주세요"
        } else {
            firestore.collection("users").document(userId)
                .collection("parking_records").document("duration")
                .set(record)
                .addOnSuccessListener {
                    _parkingRecord.value = record
                    _isEntry.value = true
                }
                .addOnFailureListener {
                    _parkingRecord.value = null
                }
        }
    }

    fun increaseCarNum(parkingLot: String) {

        val location = when (parkingLot) {
            "과학관 주차장" -> "scienceBuilding"
            "운동장 옆 주차장" -> "somethingBuilding"
            "학생회관 주차장" -> "studentCenter"
            "도서관 주차장" -> "library"
            "연구동 주차장" -> "searchBuilding"
            "산학협력관 주차장" -> "academicBuilding"
            else -> ""
        }

        val parkingDoc = firestore.collection("parkingAvailable").document(parkingLot)
        parkingDoc.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val currentLeft = document.getLong("currentLeft") ?: 0
                    val total = document.getLong("total") ?: 0
                    var updateCount = currentLeft - 1

                    if (currentLeft.toInt() == 0) {
                        _toastMessage.value = "자리가 없습니다!"
                    } else {
                        parkingDoc.update("currentLeft", updateCount)
                            .addOnSuccessListener {
                                _parkingSpace.value = updateCount.toInt()
                                _toastMessage.value = "${parkingLot}에 입차했습니다"
                            }
                            .addOnFailureListener { e ->
                                Log.e("Error", "${e.message}")
                            }
                    }
                } else {

                }
            }
            .addOnFailureListener { e ->
                Log.e("Error", "${e.message}")
            }
    }

    // Record user exit time to millis
    // Calculate duration by exitTime - entryTime
    fun recordExitTime() {
        val userId = auth.currentUser?.uid ?: return
        val exitTime = System.currentTimeMillis()


        if (_isEntry.value == false) {
            _toastMessage.value = "입차 버튼을 눌러주세요"
        } else {
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
                            _isEntry.value = true
                        }
                }
                .addOnFailureListener {
                    _parkingRecord.value = null
                }
        }

    }

    fun decreaseCarNum(parkingLot: String) {
        val parkingDoc = firestore.collection("parkingAvailable").document(parkingLot)
        parkingDoc.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val currentLeft = document.getLong("currentLeft") ?: 0
                    val updateCount = currentLeft + 1

                    parkingDoc.update("currentLeft", updateCount)
                        .addOnSuccessListener {
                            _parkingSpace.value = updateCount.toInt()
                            _toastMessage.value = "${parkingLot}에서 출차했습니다"
                        }
                        .addOnFailureListener { e ->
                            Log.e("Error", "${e.message}")
                        }
                } else {

                }
            }
            .addOnFailureListener { e ->
                Log.e("Error", "${e.message}")
            }
    }

    // Calculating Parking Fee
    private fun calculateParkingFee(durationMillis: Long) {
        val durationSecs = durationMillis / 1000
        val durationMins = durationSecs / 60
        val userId = auth.currentUser?.uid ?: return

        val parkingData = mapOf(
            "durationSecs" to durationSecs
        )

        firestore.collection("users").document(userId).collection("parking_records")
            .document("duration")
            .set(parkingData)
            .addOnSuccessListener {
                Log.d("Firebase", "Parking duration updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error updating parking duration: ${e.message}")
            }


//        _parkingFee.value = when {
//            durationMins < 30 -> "30분 무료 ${durationMins}분 주차중"
//            durationMins < 60 -> "2000원 ${durationMins}분 주차중"
//            else -> {
//                val additionalFee = ((durationMins - 60) / 30) * 500
//                "${2000 + additionalFee}원 ${durationMins}분 주차중"
//            }
//        }
        _parkingFee.value = "${durationSecs * 100}원"
    }

}