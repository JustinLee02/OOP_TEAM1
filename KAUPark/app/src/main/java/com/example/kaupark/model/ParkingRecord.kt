package com.example.kaupark.model

import kotlin.time.Duration

data class ParkingRecord(
    val entryTime: Long? = null,
    val exitTime: Long? = null,
    val duration: Long? = null,
    val date: String? = null
)
