package com.example.kaupark.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.data.ParkingClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.map.overlay.Marker

class ParkingMapViewModel(): ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val parkingClass = ParkingClass()

    private val _markers = MutableLiveData<List<Marker>>()
    val markers: LiveData<List<Marker>> get() = _markers

    private val _selectedParkingLot = MutableLiveData<String>()
    val selectedParkingLot: LiveData<String> get() = _selectedParkingLot

    private val _parkingRatio = MutableLiveData<Double>()
    val parkingRatio: LiveData<Double> get() = _parkingRatio

    init {
        loadMarkers()
    }

    private fun loadMarkers() {
        val markerList = parkingClass.getMarker()
        _markers.value = markerList
        updateMarkersWithParkingRatios(markerList)
    }

    private fun updateMarkersWithParkingRatios(markerList: List<Marker>) {
        markerList.forEach { marker ->
            val parkingLot = marker.captionText
            val docName = when(parkingLot) {
                "과학관 주차장" -> "scienceBuilding"
                "운동장 옆 주차장" -> "somethingBuilding"
                "학생회관 주차장" -> "studentCenter"
                "도서관 주차장" -> "library"
                "연구동 주차장" -> "searchBuilding"
                "산학협력관 주차장" -> "academicBuilding"
                else -> "library"
            }

            getParkingLotRatio(docName) { result ->
                marker.tag = result
            }
        }
    }

    fun selectMarker(marker: Marker) {
        _selectedParkingLot.value = marker.captionText
    }

    private fun getParkingLotRatio(parkingLot: String, callback: (String)->Unit) {
        val parkingDoc = firestore.collection("parkingAvailable").document(parkingLot)
        parkingDoc.get()
            .addOnSuccessListener { document ->
                val currentLeft = document.getLong("currentLeft")?.toDouble() ?: 0.0
                val total = document.getLong("total")?.toDouble() ?: 0.0
                val ratio: Double = (currentLeft / total) * 100
                val result = when {
                    ratio <= 50 -> "여유"
                    ratio <= 75 -> "보통"
                    ratio <= 90 -> "혼잡"
                    else -> "만차"
                }
                callback(result)
            }
            .addOnFailureListener { e ->
                Log.e("ERROR", "${e.message}")
            }
    }
}