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
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatFragment : Fragment() {
    private lateinit var currentUser: String
    private val firestore = FirebaseFirestore.getInstance()
    private val chatList = arrayListOf<Chat>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//            currentUser = it.getString("nickname").toString()
//        }
        currentUser = "9997"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChatBinding.inflate(inflater, container, false)
        Toast.makeText(context, "현재 닉네임은 ${currentUser}입니다.", Toast.LENGTH_SHORT).show()

        binding.rvList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(currentUser, chatList)
        binding.rvList.adapter = adapter


        binding.etChatting.addTextChangedListener { text ->
            binding.btnSend.isEnabled = text.toString() != ""
        }

        // 입력 버튼
        binding.btnSend.setOnClickListener {
            val chat = Chat()
            chat.nickname = currentUser
            chat.contents = binding.etChatting.text.toString()
            chat.time = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())

            firestore.collection("Chat").document().set(chat)

            binding.etChatting.setText("")

            firestore.collection("Chat")
                .get()
                .addOnSuccessListener { result ->
                    chatList.clear()
                    for (document in result) {
                        val item = Chat(document["nickname"].toString(), document["contents"].toString(), document["time"].toString())
                        chatList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
        }

        binding.chatToolbar.apply {
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener{
                parentFragmentManager.popBackStack()
            }
        }

        return binding.root
    }

}