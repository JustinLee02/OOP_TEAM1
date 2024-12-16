package com.example.kaupark.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.kaupark.model.ParkingSpotModel
import com.google.firebase.firestore.FirebaseFirestore

class ParkingAvailableViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore = FirebaseFirestore.getInstance()
    val parkingSpotList = MutableLiveData<List<ParkingSpotModel>>()
    val errorMessage = MutableLiveData<String>()

    fun loadParkingData() {
        firestore.collection("parkingAvailable")
            .get()
            .addOnSuccessListener { documents ->
                val spots = arrayListOf<ParkingSpotModel>()
                for (document in documents) {
                    val name = document.id
                    val currentLeft = document.getLong("currentLeft")?.toInt() ?: 0
                    val total = document.getLong("total")?.toInt() ?: 0

                    val parkingSpot = ParkingSpotModel(name, currentLeft, total)
                    spots.add(parkingSpot)
                }
                parkingSpotList.value = spots
            }
            .addOnFailureListener { exception ->
                errorMessage.value = "Error getting documents: ${exception.localizedMessage}"
            }
    }
}
