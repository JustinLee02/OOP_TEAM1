import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Date

class ChatViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>> get() = _chatList

    fun sendMessage(currentUser: String, receiver: String, message: String) {
        val chat = Chat().apply {
            nickname = currentUser
            contents = message
            time = Date()
        }

        firestore.collection("chattingLists")
            .whereArrayContains("participants", currentUser)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val participants = document.get("participants") as? List<String>
                    if (participants != null && participants.contains(receiver)) {
                        firestore.collection("chattingLists")
                            .document(document.id)
                            .collection("chats")
                            .add(chat)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Chat successfully added!")
                                firestore.collection("chattingLists")
                                    .document(document.id)
                                    .update(
                                        "currentTime", chat.time,
                                        "lastMessage", chat.contents
                                    )
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "currentTime successfully updated!")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("Firestore", "Error updating currentTime: $e")
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error adding chat", e)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting documents: $e")
            }
    }

    fun loadMessages(currentUser: String, receiver: String) {
        firestore.collection("chattingLists")
            .whereArrayContains("participants", currentUser)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val participants = document.get("participants") as? List<String>
                    if (participants != null && participants.contains(receiver)) {
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
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting documents: $e")
            }
    }

}
