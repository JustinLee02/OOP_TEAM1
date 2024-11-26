package com.example.kaupark.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.Person
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date

class ChattingListViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _personList = MutableLiveData<List<Person>>()
    val personList: LiveData<List<Person>> get() = _personList

    private val _carNum = MutableLiveData<String>()
    val carNum: LiveData<String> get() = _carNum

    suspend fun loadMyCarNum() {
        val userId = auth.currentUser?.uid ?: return
        val document = firestore.collection("users").document(userId).get().await()
        _carNum.value = document.getString("carNum") ?: ""
    }

    suspend fun fetchChattingList() {

        val currentCarNum = _carNum.value ?: return
        try{
            val result = firestore.collection("chattingLists")
                .whereArrayContains("participants", currentCarNum)
                .orderBy("currentTime", Query.Direction.DESCENDING)
                .get()
                .await()

            val personItems = result.documents.mapNotNull { document -> //null이 아닌 애들로만 리스트를 다시 만듦
                val participants = document.get("participants") as? MutableList<String> ?: mutableListOf()
                val currentTime = document.getDate("currentTime") ?: Date()
                Person(participants, currentTime)
            }
            _personList.value = personItems

        } catch (e: Exception) {
            Log.e("Firestore", "Error checking chatlist existence: $e")
        }


    }

    fun removePerson(position: Int) {
        _personList.value = _personList.value?.toMutableList()?.apply {
            removeAt(position)
        }
    }
}
