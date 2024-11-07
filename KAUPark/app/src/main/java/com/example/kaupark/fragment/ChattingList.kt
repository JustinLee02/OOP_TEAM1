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
    val db = FirebaseFirestore.getInstance()
    val itemList = arrayListOf<Person>()    // 리스트 아이템 배열

//    private val personList = mutableListOf(
//        Person("4130", "새로운 메세지가 왔습니다.", "pm 23:59"),
//        Person("9997", "새로운 메세지가 왔습니다.", "pm 22:00"),
//        Person("1234", "새로운 메세지가 왔습니다.", "pm 13:30"),
//        Person("1111", "새로운 메세지가 왔습니다.", "am 11:00"),
//        Person("1100", "새로운 메세지가 왔습니다.", "am 9:47")
//    )
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
        adapter = PersonsAdapter(itemList)
        binding.recChatting.layoutManager = LinearLayoutManager(context)
        binding.recChatting.adapter = adapter

        db.collection("chattingList")   // 작업할 컬렉션
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                itemList.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    val item = Person(document["carNum"] as String, document["currentTime"] as String)
                    itemList.add(item)
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

//    override fun onPersonAdded(person: Person) {
//        personList.add(0, person) // 리스트의 맨 위에 추가
//        adapter.notifyItemInserted(0) // 첫 번째 위치에 새 항목이 추가되었음을 어댑터에 알림
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
