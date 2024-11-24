import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaupark.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>> get() = _chatList

    fun sendMessage(currentUser: String, receiver: String, message: String) {
        val chat = Chat().apply {
            nickname = currentUser
            contents = message
            time = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())
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
                            .get()
                            .addOnSuccessListener { chatDocuments ->
                                val loadedChats = mutableListOf<Chat>()
                                for (chatDocument in chatDocuments) {
                                    val nickname = chatDocument.getString("nickname") ?: ""
                                    val contents = chatDocument.getString("contents") ?: ""
                                    val time = chatDocument.getString("time") ?: ""
                                    loadedChats.add(Chat(nickname, contents, time))
                                }
                                _chatList.value = loadedChats
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "Error getting chats: $e")
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting documents: $e")
            }
    }
}
