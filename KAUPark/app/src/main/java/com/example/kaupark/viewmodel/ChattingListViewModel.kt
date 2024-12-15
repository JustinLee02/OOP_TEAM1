package com.example.kaupark.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.PersonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date

class ChattingListViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _personList = MutableLiveData<List<PersonModel>>()
    val personList: LiveData<List<PersonModel>> get() = _personList

    private val _carNum = MutableLiveData<String>()
    val carNum: LiveData<String> get() = _carNum

    suspend fun loadMyCarNum() {
        val userId = auth.currentUser?.uid ?: return
        val document = firestore.collection("users").document(userId).get().await()
        _carNum.value = document.getString("carNum") ?: ""
    }

    suspend fun fetchChattingList() {
        val currentCarNum = _carNum.value ?: return
        val result = firestore.collection("chattingLists")
            .whereArrayContains("participants", currentCarNum)
            .orderBy("currentTime", Query.Direction.DESCENDING)
            .get()
            .await()

            val personItems = result.documents.mapNotNull { document ->
                val participants = document.get("participants") as? MutableList<String> ?: mutableListOf()
                val currentTime = document.getDate("currentTime") ?: Date()
                val lastMessage = document.getString("lastMessage") ?: "새로운 메세지가 왔습니다"

                PersonModel(participants, currentTime,lastMessage)
            }

        _personList.value = personItems

    }
    suspend fun deleteChattingList(person: PersonModel, receiver: String) {
        // 1. Firestore에서 해당 문서 찾기
        val result = firestore.collection("chattingLists")
            .whereArrayContains("participants", receiver)
            .get()
            .await()

        // 2. 찾은 문서의 chats 서브 컬렉션 삭제
        result.documents.firstOrNull()?.let { document ->
            val chatsCollection = firestore.collection("chattingLists")
                .document(document.id)
                .collection("chats")

            // chats 서브 컬렉션에 있는 모든 문서 삭제
            val chats = chatsCollection.get().await()
            for (chatDoc in chats) {
                chatsCollection.document(chatDoc.id).delete().await()
            }

            // 3. chats 삭제 후 chattingLists 문서 삭제
            firestore.collection("chattingLists")
                .document(document.id)
                .delete()
                .await()
        }
    }



    fun removePerson(position: Int) {
        _personList.value = _personList.value?.toMutableList()?.apply {
            removeAt(position)
        }
    }
}
