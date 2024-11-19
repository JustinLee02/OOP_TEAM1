package com.example.kaupark.model

data class Person(
    var participants: MutableList<String> = mutableListOf("unknown1","unknown2"),
//    var sender: String = "Unknown",
//    var receiver: String = "Unknown",
    var currentTime: String = "am 00:00"
)
