package com.example.kaupark.model

import java.util.Date

data class Chat(
    var nickname: String? = null,
    var contents: String? = null,
    var time: Date? = null
)

