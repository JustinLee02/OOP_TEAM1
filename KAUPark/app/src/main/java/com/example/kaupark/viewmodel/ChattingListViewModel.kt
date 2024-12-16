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

// ViewModel 클래스: 채팅 목록 관리를 담당
class ChattingListViewModel : ViewModel() {

    // Firebase Firestore 인스턴스 생성
    private val firestore = FirebaseFirestore.getInstance()

    // Firebase 인증 인스턴스 생성
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // MutableLiveData: 채팅 참여자 목록을 실시간으로 저장 및 관찰
    private val _personList = MutableLiveData<List<PersonModel>>()
    val personList: LiveData<List<PersonModel>> get() = _personList // 외부에서는 읽기만 가능하도록 공개

    // MutableLiveData: 사용자 차량 번호를 실시간으로 저장 및 관찰
    private val _carNum = MutableLiveData<String>()
    val carNum: LiveData<String> get() = _carNum // 외부에서는 읽기만 가능하도록 공개

    // suspend 함수: Firebase에서 현재 로그인된 사용자의 차량 번호를 불러오는 작업
    suspend fun loadMyCarNum() {
        val userId = auth.currentUser?.uid ?: return // 현재 사용자가 로그인되어 있지 않으면 리턴
        val document = firestore.collection("users").document(userId).get().await() // Firestore에서 사용자 문서 가져오기
        _carNum.value = document.getString("carNum") ?: "" // 문서에서 'carNum' 필드 가져와서 MutableLiveData에 설정
    }

    // suspend 함수: Firestore에서 해당 차량 번호와 관련된 채팅 목록을 가져오는 작업
    suspend fun fetchChattingList() {
        val currentCarNum = _carNum.value ?: return // 차량 번호가 null인 경우 작업 중단
        val result = firestore.collection("chattingLists")
            .whereArrayContains("participants", currentCarNum) // participants 배열에 해당 차량 번호가 포함된 문서 검색
            .orderBy("currentTime", Query.Direction.DESCENDING) // 최신 메시지가 있는 순서대로 정렬
            .get()
            .await() // Firestore 쿼리 결과 대기

        // Firestore 문서를 PersonModel 객체로 매핑
        val personItems = result.documents.mapNotNull { document ->
            val participants = document.get("participants") as? MutableList<String> ?: mutableListOf() // 참여자 목록 가져오기
            val currentTime = document.getDate("currentTime") ?: Date() // 최근 메시지 시간 가져오기
            val lastMessage = document.getString("lastMessage") ?: "새로운 메세지가 왔습니다" // 최근 메시지 내용 가져오기

            PersonModel(participants, currentTime, lastMessage) // PersonModel 객체 생성
        }

        _personList.value = personItems // MutableLiveData에 채팅 목록 설정
    }

    // suspend 함수: Firestore에서 특정 채팅 목록과 관련된 데이터를 삭제하는 작업
    suspend fun deleteChattingList(person: PersonModel, receiver: String) {
        // 1. Firestore에서 해당 채팅 목록 문서 검색 (participants 배열에 receiver가 포함된 문서)
        val result = firestore.collection("chattingLists")
            .whereArrayContains("participants", receiver)
            .get()
            .await()

        // 2. 찾은 채팅 문서의 chats 서브 컬렉션 삭제
        result.documents.firstOrNull()?.let { document -> // 첫 번째 관련 문서를 가져옴
            val chatsCollection = firestore.collection("chattingLists")
                .document(document.id)
                .collection("chats") // chats 서브 컬렉션 참조

            // chats 서브 컬렉션의 모든 문서 삭제
            val chats = chatsCollection.get().await() // chats 문서 가져오기
            for (chatDoc in chats) {
                chatsCollection.document(chatDoc.id).delete().await() // chats 문서 각각 삭제
            }

            // 3. chats 삭제 후 chattingLists 문서 자체 삭제
            firestore.collection("chattingLists")
                .document(document.id)
                .delete()
                .await()
        }
    }

    // 채팅 참여자를 삭제하는 함수 (UI 갱신용)
    fun removePerson(position: Int) {
        _personList.value = _personList.value?.toMutableList()?.apply {
            removeAt(position) // 지정된 위치의 참여자 제거
        }
    }
}
