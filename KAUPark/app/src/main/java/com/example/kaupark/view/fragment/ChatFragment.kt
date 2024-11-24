package com.example.kaupark.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaupark.ChatAdapter
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentChatBinding
import com.example.kaupark.model.Chat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragment : Fragment() {
    private lateinit var currentUser: String
    private lateinit var receiver: String
    var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val chatList = arrayListOf<Chat>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUser = arguments?.getString("currentUser").toString()
        receiver = arguments?.getString("receiver").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentChatBinding.inflate(inflater, container, false)
        Toast.makeText(context, "현재 닉네임은 ${currentUser}입니다.", Toast.LENGTH_SHORT).show()

        binding.recyclerviewChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(currentUser, chatList)
        binding.recyclerviewChat.adapter = adapter


        binding.edittextMessage.addTextChangedListener { text ->
            binding.buttonSendmessage.isEnabled = text.toString() != ""
        }
        callMessage()
        // 입력 버튼
        binding.buttonSendmessage.setOnClickListener {
            val chat = Chat().apply {
                nickname = currentUser
                contents = binding.edittextMessage.text.toString()
                time = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())
            }

            firestore.collection("chattingLists")
                .whereArrayContains("participants",currentUser)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // 두 번째 조건 확인
                        val participants = document.get("participants") as? List<String>
                        if (participants != null && participants.contains(receiver)) {
                            // 해당 문서 ID로 하위 컬렉션에 데이터 추가
                            firestore.collection("chattingLists")
                                .document(document.id) // 조건에 맞는 문서 ID 사용
                                .collection("chats") // 하위 컬렉션
                                .add(chat) // 데이터 추가
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

            binding.edittextMessage.setText("")
            callMessage()

        }

        binding.toolbarChat.apply {
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener{
                parentFragmentManager.popBackStack()
            }
        }

        return binding.root
    }

    private fun callMessage(){
        firestore.collection("chattingLists")
            .whereArrayContains("participants", currentUser) // 첫 번째 조건: 현재 사용자 포함
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val participants = document.get("participants") as? List<String>
                    if (participants != null && participants.contains(receiver)) { // 두 번째 조건: 상대방 포함
                        // 조건에 맞는 문서 ID로 하위 컬렉션 데이터 가져오기
                        firestore.collection("chattingLists")
                            .document(document.id) // 조건에 맞는 문서 ID
                            .collection("chats") // 하위 컬렉션 "chats"
                            .orderBy("time", Query.Direction.ASCENDING)
                            .get()
                            .addOnSuccessListener { chatDocuments ->
                                chatList.clear()
                                for (chatDocument in chatDocuments) {
                                    val nickname = chatDocument.getString("nickname") ?: ""
                                    val contents = chatDocument.getString("contents") ?: ""
                                    val time = chatDocument.getString("time") ?: ""

                                    // 가져온 데이터를 리스트에 추가
                                    chatList.add(Chat(nickname, contents, time))
                                }
                                // 어댑터 갱신
                                adapter.notifyDataSetChanged()
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



    companion object {

        @JvmStatic
        fun newInstance(carNum1: String, carNum2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply{
                    carNum1.let {
                        putString("currentUser",it)
                    }
                    carNum2.let {
                        putString("receiver",it)
                    }
                }
        }
    }

}