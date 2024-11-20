package com.example.kaupark.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.data.ParkingClass
import com.naver.maps.map.overlay.Marker

class ParkingMapViewModel(): ViewModel() {

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
    }

    fun selectMarker(marker: Marker) {
        _selectedParkingLot.value = marker.captionText
    }
}