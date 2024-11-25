package com.example.kaupark.viewmodel

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
        // Firestore에서 chattingLists 컬렉션을 가져오고, currentTime을 기준으로 정렬
        val snapshot = firestore.collection("chattingLists")
            .orderBy("currentTime", Query.Direction.DESCENDING)  // 시간순으로 정렬
            .get()
            .await()

        val personItems = snapshot.documents.mapNotNull { document ->
            val participants = document.get("participants") as? List<String> ?: emptyList()
            val currentTime = document.getDate("currentTime") ?: Date() // Timestamp를 Date로 변환

            Person(participants.toMutableList(), currentTime)
        }



        // 정렬된 리스트를 _personList에 할당
        _personList.value = personItems
    }

    fun removePerson(position: Int) {
        _personList.value = _personList.value?.toMutableList()?.apply {
            removeAt(position)
        }
    }
}
