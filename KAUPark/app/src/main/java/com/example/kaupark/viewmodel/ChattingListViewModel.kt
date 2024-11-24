package com.example.kaupark.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.Person
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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

    fun fetchChattingList() {
        val currentCarNum = _carNum.value ?: return

        firestore.collection("chattingLists")
            .whereArrayContains("participants", currentCarNum)
            .get()
            .addOnSuccessListener { result ->
                val personItems = result.documents.mapNotNull { document ->
                    val participants = document.get("participants") as? MutableList<String> ?: mutableListOf()
                    val currentTime = document.getString("currentTime").orEmpty()
                    Person(participants, currentTime)
                }
                _personList.value = personItems
            }
            .addOnFailureListener { exception ->
                // 실패 처리 (필요시)
            }
    }

    fun removePerson(position: Int) {
        _personList.value = _personList.value?.toMutableList()?.apply {
            removeAt(position)
        }
    }
}
