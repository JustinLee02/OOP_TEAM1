package com.example.kaupark.model

import java.util.Date

data class PersonModel(
    var participants: MutableList<String> = mutableListOf("unknown1","unknown2"),
    var currentTime: Date? = null,
    var lastMessage: String? = null
)