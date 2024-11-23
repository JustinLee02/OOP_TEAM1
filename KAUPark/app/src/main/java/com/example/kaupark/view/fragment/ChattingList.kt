package com.example.kaupark.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.ChattingListBinding
import com.example.kaupark.model.Person
import com.example.kaupark.PersonsAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChattingList : Fragment(){

    val firestore = FirebaseFirestore.getInstance()
    var auth : FirebaseAuth = Firebase.auth
    val personList = arrayListOf<Person>()

    private lateinit var adapter: PersonsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ChattingListBinding.inflate(inflater, container, false)
        adapter = PersonsAdapter(personList)
        binding.recChatting.layoutManager = LinearLayoutManager(context)
        binding.recChatting.adapter = adapter
        val carNum = "9997"
        firestore.collection("chattingLists")
            .whereArrayContains("participants", carNum)
            .get()
            .addOnSuccessListener { result ->
                personList.clear() //클리어하는 이유는 안하면은 리스트에 중복된 값이 계속 쌓여서 그럼
                for (document in result.documents) { //모든걸 가져와야하니까 for문
                    val participants = document.get("participants") as? MutableList<String> ?: mutableListOf()
                    val currentTime = document.getString("currentTime").orEmpty()
                    val item = Person(participants, currentTime)
                    personList.add(item)
                }
                adapter.notifyDataSetChanged() // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("ChattingList", "Error getting documents: $exception")
            }

        binding.chatplusBtn.setOnClickListener {
            val chatPopupFragment = ChatPopupFragment()
            chatPopupFragment.show(parentFragmentManager, "ChatPopupFragment")
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                adapter.removePerson(position)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recChatting)
        return binding.root
    }

//    private fun getMyCarNum():String {
//        val userId = auth.currentUser?.uid ?: return ""
//
//        firestore.collection("user").document(userId).get()
//        val carNum = document.getString("carNum")
//        return carNum
//    }

}
