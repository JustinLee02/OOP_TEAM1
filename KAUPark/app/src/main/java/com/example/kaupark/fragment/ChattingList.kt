package com.example.kaupark.fragment

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
import com.google.firebase.database.core.RepoManager.clear
import com.google.firebase.firestore.FirebaseFirestore

class ChattingList : Fragment(){

    private var _binding: ChattingListBinding? = null
    private val binding get() = _binding!!
    val firestore = FirebaseFirestore.getInstance()
    val personList = arrayListOf<Person>()

    private lateinit var adapter: PersonsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChattingListBinding.inflate(inflater, container, false)
        setupChattingList()
        return binding.root
    }

    private fun setupChattingList() {
        adapter = PersonsAdapter(personList)
        binding.recChatting.layoutManager = LinearLayoutManager(context)
        binding.recChatting.adapter = adapter

        //personList.sortBy { T -> T.currentTime }

        firestore.collection("chattingList")   // 작업할 컬렉션
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                personList.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    val item = Person(document["carNum"] as String, document["currentTime"] as String)
                    personList.add(item)
                }
                adapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("MainActivity", "Error getting documents: $exception")
            }

        binding.chatplusBtn.setOnClickListener {
            val chatPopupFragment = ChatPopupFragment()
            chatPopupFragment.setTargetFragment(this, 0) // ChattingList를 targetFragment로 설정
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
