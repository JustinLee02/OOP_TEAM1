package com.example.kaupark.fragment

import android.os.Bundle
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

class ChattingList : Fragment(), ChatPopupFragment.OnPersonAddedListener {

    private var _binding: ChattingListBinding? = null
    private val binding get() = _binding!!

    private val personList = mutableListOf(
        Person("4130", "새로운 메세지가 왔습니다.", "pm 23:59"),
        Person("9997", "새로운 메세지가 왔습니다.", "pm 22:00"),
        Person("1234", "새로운 메세지가 왔습니다.", "pm 13:30"),
        Person("1111", "새로운 메세지가 왔습니다.", "am 11:00"),
        Person("1100", "새로운 메세지가 왔습니다.", "am 9:47")
    )
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

        binding.chatplusBtn.setOnClickListener {
            val chatPopupFragment = ChatPopupFragment()
            chatPopupFragment.setTargetFragment(this, 0) // ChattingList를 targetFragment로 설정
            chatPopupFragment.show(parentFragmentManager, "ChatPopupFragment")
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
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

    override fun onPersonAdded(person: Person) {
        personList.add(0, person) // 리스트의 맨 위에 추가
        adapter.notifyItemInserted(0) // 첫 번째 위치에 새 항목이 추가되었음을 어댑터에 알림
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
