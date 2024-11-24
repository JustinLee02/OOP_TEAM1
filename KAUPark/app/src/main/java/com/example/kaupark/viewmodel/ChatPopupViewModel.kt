package com.example.kaupark.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.Person
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatPopupViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getCurrentUserCarNum(): String {
        val userId = auth.currentUser?.uid ?: return ""
        val document = firestore.collection("users").document(userId).get().await()
        return document.getString("carNum") ?: ""
    }

    suspend fun doesChatExist(carNum1: String, carNum2: String): Boolean {
        return try {
            val documents = firestore.collection("chattingLists")
                .whereArrayContains("participants", carNum1)
                .get()
                .await()

            documents.any { document ->
                val participants = document.get("participants") as? List<String>
                participants?.contains(carNum2) == true
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error checking chat existence: $e")
            false
        }
    }

    suspend fun doesCarNumExist(carNum: String): Boolean {
        return try {
            val documents = firestore.collection("users")
                .whereEqualTo("carNum", carNum)
                .get()
                .await()
            documents.isEmpty.not()
        } catch (e: Exception) {
            Log.e("Firestore", "Error checking car number existence: $e")
            false
        }
    }

    suspend fun createChat(carNum1: String, carNum2: String) {
        try {
            val person = Person(
                participants = mutableListOf(carNum1, carNum2),
                currentTime = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())
            )

            firestore.collection("chattingLists").document().set(person).await()
        } catch (e: Exception) {
            Log.e("Firestore", "Error creating chat: $e")
        }
    }
}
