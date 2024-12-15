package com.example.kaupark.model

data class UserModel(
    val name: String,
    val studentId: String,
    val password: String,
    val phoneNum: String,
    val email: String,
    val carNum: String,
    val deposit: Int
)
