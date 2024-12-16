import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.ChatModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date

// ViewModel 클래스: 채팅 메시지 전송 및 로드와 관련된 로직을 처리
class ChatViewModel : ViewModel() {

    // Firestore 인스턴스 초기화
    private val firestore = FirebaseFirestore.getInstance()

    // LiveData를 사용하여 UI에 채팅 데이터 제공
    private val _chatList = MutableLiveData<List<ChatModel>>()
    val chatList: LiveData<List<ChatModel>> get() = _chatList

    // suspend 함수: 메시지를 전송하는 로직
    suspend fun sendMessage(currentUser: String, receiver: String, message: String) {
        // 전송할 채팅 메시지 데이터 생성
        val chat = ChatModel().apply {
            nickname = currentUser
            contents = message
            time = Date() // 현재 시간 저장
        }

        try {
            // 현재 사용자가 포함된 채팅방 검색
            val documents = firestore.collection("chattingLists")
                .whereArrayContains("participants", currentUser)
                .get()
                .await() // 비동기로 Firestore 작업 결과 대기

            // 검색된 채팅방 중 receiver가 포함된 채팅방 찾기
            for (document in documents) {
                val participants = document.get("participants") as? List<String>
                if (participants != null && participants.contains(receiver)) {
                    // 해당 채팅방에 메시지 추가
                    firestore.collection("chattingLists")
                        .document(document.id) // 채팅방의 ID로 접근
                        .collection("chats") // 하위 컬렉션 'chats'
                        .add(chat)
                        .await() // 메시지 전송 완료 대기

                    Log.d("Firestore", "Chat successfully added!")

                    // 채팅방의 currentTime과 lastMessage 업데이트
                    firestore.collection("chattingLists")
                        .document(document.id)
                        .update(
                            "currentTime", chat.time,
                            "lastMessage", chat.contents
                        )
                        .await() // 업데이트 완료 대기

                    Log.d("Firestore", "currentTime successfully updated!")
                }
            }
        } catch (e: Exception) {
            // 에러 처리: 로그 출력
            Log.w("Firestore", "Error in sendMessage: $e")
        }
    }

    // suspend 함수: 채팅 메시지 로드
    suspend fun loadMessages(currentUser: String, receiver: String) {
        try {
            // 현재 사용자가 포함된 채팅방 검색
            val documents = firestore.collection("chattingLists")
                .whereArrayContains("participants", currentUser)
                .get()
                .await() // 비동기로 Firestore 작업 결과 대기

            // 검색된 채팅방 중 receiver가 포함된 채팅방 찾기
            for (document in documents) {
                val participants = document.get("participants") as? List<String>
                if (participants != null && participants.contains(receiver)) {
                    // 해당 채팅방의 'chats' 하위 컬렉션을 실시간으로 청취
                    firestore.collection("chattingLists")
                        .document(document.id) // 채팅방의 ID로 접근
                        .collection("chats") // 하위 컬렉션 'chats'
                        .orderBy("time", Query.Direction.ASCENDING) // 시간 순으로 정렬
                        .addSnapshotListener { chatDocuments, e ->
                            if (e != null) {
                                // 에러 처리: 로그 출력
                                Log.w("Firestore", "Error listening to chat updates", e)
                                return@addSnapshotListener
                            }

                            // 새로운 채팅 데이터가 있다면 LiveData 업데이트
                            if (chatDocuments != null) {
                                val loadedChats = mutableListOf<ChatModel>()
                                for (chatDocument in chatDocuments) {
                                    val nickname = chatDocument.getString("nickname") ?: ""
                                    val contents = chatDocument.getString("contents") ?: ""
                                    val time = chatDocument.getDate("time")
                                    loadedChats.add(ChatModel(nickname, contents, time))
                                }
                                _chatList.value = loadedChats // LiveData 업데이트
                            }
                        }
                }
            }
        } catch (e: Exception) {
            // 에러 처리: 로그 출력
            Log.w("Firestore", "Error loading messages: $e")
        }
    }
}
