package com.example.kaupark.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.data.ParkingClass
import com.example.kaupark.model.ParkingRecordModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.map.overlay.Marker


/**
 * HomewViewModel
 * Description: HomeView Fragment와 연결되어 LiveData를 통해 데이터 변경을 UI에 반영
 */
class HomeViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // LiveData 변수 선언 (UI 상태를 위한 변수)
    private val _userCarNum = MutableLiveData<String>() // 사용자의 차량 번호
    val userCarNum: LiveData<String> get() = _userCarNum

    private val _userName = MutableLiveData<String>() // 사용자의 이름
    val userName: LiveData<String> get() = _userName

    private val _parkingRecord = MutableLiveData<ParkingRecordModel?>() // 주차 기록
    val parkingRecord: LiveData<ParkingRecordModel?> get() = _parkingRecord

    private val _parkingFee = MutableLiveData<String>() // 주차 요금
    val parkingFee: LiveData<String> get() = _parkingFee

    private val _parkingSpace = MutableLiveData<Int>() // 남은 주차 공간 수
    val parkingSpace: LiveData<Int> get() = _parkingSpace

    private val _isEntry = MutableLiveData<Boolean>() // 주차장 입차 여부
    val isEntry: LiveData<Boolean> get() = _isEntry

    private val _toastMessage = MutableLiveData<String?>() // 사용자에게 보여줄 토스트 메세지
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _markers = MutableLiveData<List<Marker>>() // 지도에 표시할 마커 목록
    val markers: LiveData<List<Marker>> get() = _markers

    private val _selectedParkingLot = MutableLiveData<String>() // 선택된 주차장 이름
    val selectedParkingLot: LiveData<String> get() = _selectedParkingLot

    private val _parkingRatio = MutableLiveData<String>() // 주차장 혼잡도 비율
    val parkingRatio: LiveData<String> get() = _parkingRatio


    init {
        _isEntry.value = false
        loadMarkers()
    }

    /**
     * Firestore에서 사용자 정보를 가져와 LiveData에 저장
     */
    fun fetchUserInfo() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Firestore에서 사용자 이름과 차량 번호를 가져와 LiveData에 저장
                    val carNum = document.getString("carNum") ?: "차량 번호 없음"
                    val name = document.getString("name") ?: "이름 없음"

                    _userCarNum.value = carNum
                    _userName.value = name
                } else {
                    // ToastHelper.showToast()
                }
            }
            .addOnFailureListener { e ->
                // Toast.makeText(this, "실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    /**
     * 사용자가 주차장에 입차했을 때 시간을 기록
     * Firestore 에 입차 시간과 날짜를 저장
     */

    fun recordEntryTime() {
        val userId = auth.currentUser?.uid ?: return // 현재 로그인된 사용자 ID 가져오기
        val entryTime = System.currentTimeMillis()
        val currentDate = android.text.format.DateFormat.format("yyyy-MM-dd", entryTime).toString() // 현재 날짜 포맷팅

        val record = ParkingRecordModel(entryTime = entryTime, date = currentDate) // 주차 기록 객체 생성
        if (_isEntry.value == true) {
            _toastMessage.value = "출차 버튼을 눌러주세요"
        } else {
            val documentId = entryTime.toString()

            firestore.collection("users").document(userId)
                .collection("parking_records").document(documentId)
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

    /**
     * 특정 주차장의 차량 수를 증가시키고 남은 공간을 업데이트
     */
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

        val parkingDoc = firestore.collection("parkingAvailable").document(location)
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

    /**
     * 사용자가 출차했을 때 시간을 기록하고 주차 요금을 계산
     */
    fun recordExitTime() {
        val userId = auth.currentUser?.uid ?: return
        val exitTime = System.currentTimeMillis()


        if (_isEntry.value == false) {
            _toastMessage.value = "입차 버튼을 눌러주세요"
        } else {
            firestore.collection("users").document(userId)
                .collection("parking_records")
                .orderBy("entryTime", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    val document = documents.firstOrNull()
                    val entryTime = document?.getLong("entryTime") ?: return@addOnSuccessListener
                    val duration = exitTime - entryTime
                    val date = document.getString("date")

                    val updatedRecord = ParkingRecordModel(entryTime, exitTime, duration, date)
                    val documentId = entryTime.toString()
                    firestore.collection("users").document(userId)
                        .collection("parking_records").document(documentId)
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

    /**
     * 주차 요금을 계산하여 LiveData에 저장
     */
    private fun calculateParkingFee(durationMillis: Long) {
        val durationSecs = durationMillis / 1000
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

        /*
        _parkingFee.value = when {
            durationMins < 30 -> "30분 무료 ${durationMins}분 주차중"
            durationMins < 60 -> "2000원 ${durationMins}분 주차중"
            else -> {
                val additionalFee = ((durationMins - 60) / 30) * 500
                "${2000 + additionalFee}원 ${durationMins}분 주차중"
            }
        } */

        _parkingFee.value = "${durationSecs * 100}원"
    }

    /**
     * 주차장 마커를 로드하고 혼잡도를 업데이트
     */
    private fun loadMarkers() {
        val markerList = ParkingClass().getMarker()
        _markers.value = markerList
        updateMarkersWithParkingRatios(markerList)
    }

    /**
     * 주차장 혼잡도를 계산하여 마커에 표시
     */
    private fun updateMarkersWithParkingRatios(markerList: List<Marker>) {
        markerList.forEach { marker ->
            val parkingLot = marker.captionText
            val docName = when (parkingLot) {
                "과학관 주차장" -> "scienceBuilding"
                "운동장 옆 주차장" -> "somethingBuilding"
                "학생회관 주차장" -> "studentCenter"
                "도서관 주차장" -> "library"
                "연구동 주차장" -> "searchBuilding"
                "산학협력관 주차장" -> "academicBuilding"
                else -> "unknown"
            }

            getParkingLotRatio(docName) { result ->
                marker.tag = result
            }
        }
    }

    /**
     * 주차장의 혼잡도 비율을 계산하여 콜백으로 반환
     */
    private fun getParkingLotRatio(parkingLot: String, callback: (String) -> Unit) {
        firestore.collection("parkingAvailable").document(parkingLot)
            .get()
            .addOnSuccessListener { document ->
                val currentLeft = document.getLong("currentLeft")?.toDouble() ?: 0.0
                val total = document.getLong("total")?.toDouble() ?: 0.0
                val ratio: Double = ((total - currentLeft) / total) * 100
                val result = when {
                    ratio <= 50 -> "여유"
                    ratio <= 75 -> "보통"
                    ratio <= 90 -> "혼잡"
                    else -> "만차"
                }
                callback(result)
            }
            .addOnFailureListener { e ->
                Log.e("Firebase Error", "Error getting parking lot ratio: ${e.message}")
            }
    }

    /**
     * 사용자가 선택한 주차장을 LiveData에 저장
     */
    fun selectMarker(marker: Marker) {
        _selectedParkingLot.value = marker.captionText
    }

}