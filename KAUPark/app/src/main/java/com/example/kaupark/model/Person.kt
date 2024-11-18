package com.example.kaupark.model

data class Person(
    var participants: MutableList<String> = mutableListOf("unknown1","unknown2"),
    var currentTime: String? = null
)
