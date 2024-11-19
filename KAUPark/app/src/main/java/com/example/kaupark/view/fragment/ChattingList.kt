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
import com.google.firebase.firestore.FirebaseFirestore

class ChattingList : Fragment(){

    val firestore = FirebaseFirestore.getInstance()
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
                // 성공할 경우
                for (document in result.documents) {
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

}
