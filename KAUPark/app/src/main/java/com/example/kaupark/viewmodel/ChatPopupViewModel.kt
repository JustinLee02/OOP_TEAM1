package com.example.kaupark.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.PersonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date

// ViewModel 클래스: 팝업 화면에서 채팅 관련 로직을 처리
class ChatPopupViewModel : ViewModel() {

    // Firebase 인증 및 Firestore 인스턴스 초기화
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // suspend 함수: 현재 사용자의 차량 번호를 Firestore에서 가져옴
    suspend fun getCurrentUserCarNum(): String {
        val userId = auth.currentUser?.uid ?: return "" // 현재 로그인된 사용자의 ID가 없으면 빈 문자열 반환
        val document = firestore.collection("users").document(userId).get().await() // Firestore에서 사용자 문서 가져오기
        return document.getString("carNum") ?: "" // 'carNum' 필드 값 반환 (없으면 빈 문자열)
    }

    // suspend 함수: 특정 두 차량 번호 간의 채팅 목록이 존재하는지 확인
    suspend fun doesChatExist(carNum1: String, carNum2: String): Boolean {
        return try {
            // Firestore에서 'participants' 배열에 carNum1이 포함된 문서 검색
            val documents = firestore.collection("chattingLists")
                .whereArrayContains("participants", carNum1)
                .get()
                .await()

            // 검색된 문서 중 participants 배열에 carNum2가 포함된 문서가 있는지 확인
            documents.any { document ->
                val participants = document.get("participants") as? List<String>
                participants?.contains(carNum2) == true // carNum2가 포함된 경우 true 반환
            }
        } catch (e: Exception) {
            // 예외 처리: 에러 로그 출력 후 false 반환
            Log.e("Firestore", "Error checking chat existence: $e")
            false
        }
    }

    // suspend 함수: 특정 차량 번호가 Firestore에 존재하는지 확인
    suspend fun doesCarNumExist(carNum: String): Boolean {
        return try {
            // Firestore에서 'carNum' 필드가 carNum과 일치하는 문서 검색
            val documents = firestore.collection("users")
                .whereEqualTo("carNum", carNum)
                .get()
                .await()

            // 문서가 하나라도 존재하면 true 반환
            documents.isEmpty.not()
        } catch (e: Exception) {
            // 예외 처리: 에러 로그 출력 후 false 반환
            Log.e("Firestore", "Error checking car number existence: $e")
            false
        }
    }

    // suspend 함수: Firestore에 새로운 채팅 방 생성
    suspend fun createChat(carNum1: String, carNum2: String) {
        try {
            // 새로운 PersonModel 객체 생성 및 값 설정
            val person = PersonModel().apply {
                participants = mutableListOf(carNum1, carNum2) // 참여자 리스트 설정
                currentTime = Date() // 현재 시간 설정
            }

            // Firestore에 'chattingLists' 컬렉션에 문서 추가
            firestore.collection("chattingLists").document().set(person).await()
        } catch (e: Exception) {
            // 예외 처리: 에러 로그 출력
            Log.e("Firestore", "Error creating chat: $e")
        }
    }
}
