package com.example.kaupark.model

data class ParkingSpotModel(
    var name: String = "unknown",
    var currentLeft: Int = 0,
    var total: Int = 0
)
