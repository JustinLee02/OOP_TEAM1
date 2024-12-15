import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date

class ChatViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>> get() = _chatList

    suspend fun sendMessage(currentUser: String, receiver: String, message: String) {
        val chat = Chat().apply {
            nickname = currentUser
            contents = message
            time = Date()
        }

        try {
            // participants 필드를 기반으로 채팅방을 찾기
            val documents = firestore.collection("chattingLists")
                .whereArrayContains("participants", currentUser)
                .get()
                .await()  // 비동기적으로 결과를 기다림

            // 채팅방이 존재한다면, 채팅 메시지를 추가
            for (document in documents) {
                val participants = document.get("participants") as? List<String>
                if (participants != null && participants.contains(receiver)) {
                    // 채팅 메시지 추가
                    firestore.collection("chattingLists")
                        .document(document.id)
                        .collection("chats")
                        .add(chat)
                        .await()  // 채팅 추가가 완료될 때까지 기다림

                    Log.d("Firestore", "Chat successfully added!")

                    // currentTime과 lastMessage 업데이트
                    firestore.collection("chattingLists")
                        .document(document.id)
                        .update(
                            "currentTime", chat.time,
                            "lastMessage", chat.contents
                        )
                        .await()  // 업데이트 완료를 기다림

                    Log.d("Firestore", "currentTime successfully updated!")
                }
            }
        } catch (e: Exception) {
            Log.w("Firestore", "Error in sendMessage: $e")
        }
    }

    suspend fun loadMessages(currentUser: String, receiver: String) {
        try {
            // chattingLists에서 채팅방 목록을 가져옴
            val documents = firestore.collection("chattingLists")
                .whereArrayContains("participants", currentUser)
                .get()
                .await()  // 코루틴으로 비동기적으로 대기

            // 채팅방을 확인하여 receiver가 참가자인지 확인
            for (document in documents) {
                val participants = document.get("participants") as? List<String>
                if (participants != null && participants.contains(receiver)) {
                    // 해당 채팅방에 대한 채팅 기록을 실시간으로 업데이트
                    firestore.collection("chattingLists")
                        .document(document.id)
                        .collection("chats")
                        .orderBy("time", Query.Direction.ASCENDING)
                        .addSnapshotListener { chatDocuments, e ->
                            if (e != null) {
                                Log.w("Firestore", "Error listening to chat updates", e)
                                return@addSnapshotListener
                            }

                            if (chatDocuments != null) {
                                val loadedChats = mutableListOf<Chat>()
                                for (chatDocument in chatDocuments) {
                                    val nickname = chatDocument.getString("nickname") ?: ""
                                    val contents = chatDocument.getString("contents") ?: ""
                                    val time = chatDocument.getDate("time")
                                    loadedChats.add(Chat(nickname, contents, time))
                                }
                                _chatList.value = loadedChats
                            }
                        }
                }
            }
        } catch (e: Exception) {
            Log.w("Firestore", "Error loading messages: $e")
        }
    }

}
