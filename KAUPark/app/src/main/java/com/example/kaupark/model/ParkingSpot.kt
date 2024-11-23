package com.example.kaupark.model

data class ParkingSpot(
    var name: String = "unknown",
    var currentLeft: Int = 0,
    var total: Int = 0
)
