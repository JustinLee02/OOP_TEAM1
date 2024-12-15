package com.example.kaupark.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.ChattingListBinding
import com.example.kaupark.PersonsAdapter
import com.example.kaupark.R
import com.example.kaupark.viewmodel.ChattingListViewModel
import kotlinx.coroutines.launch

class ChattingList : Fragment() {

    private val viewModel: ChattingListViewModel by viewModels()
    private lateinit var binding: ChattingListBinding
    private lateinit var adapter: PersonsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChattingListBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupObservers()

        lifecycleScope.launch {
            viewModel.loadMyCarNum()
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
                val person = viewModel.personList.value?.get(position) ?: return

// 해당 문서를 Firestore에서 삭제
                val carNum = viewModel.carNum.value ?: return
                val receiver = if (person.participants[0] == carNum) person.participants[1] else person.participants[0]

                lifecycleScope.launch {
                    // chattingLists에서 해당 문서 삭제
                    viewModel.deleteChattingList(person, receiver) // Firestore에서 삭제
                    viewModel.removePerson(position) // 로컬 리스트에서 삭제
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recChatting)
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recChatting.layoutManager = LinearLayoutManager(context)

        adapter = PersonsAdapter(mutableListOf(), "") { person ->
            val carNum = viewModel.carNum.value ?: ""
            val receiver = if (person.participants[0] == carNum) person.participants[1] else person.participants[0]
            moveToChatFragment(carNum, receiver)
        }
        binding.recChatting.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.carNum.observe(viewLifecycleOwner) { carNum ->
            adapter = PersonsAdapter(mutableListOf(), carNum) { person ->
                val receiver = if (person.participants[0] == carNum) person.participants[1] else person.participants[0]
                moveToChatFragment(carNum, receiver)
            }
            binding.recChatting.adapter = adapter

            lifecycleScope.launch {
                viewModel.fetchChattingList()
            }

        }

        viewModel.personList.observe(viewLifecycleOwner) { updatedList ->
            adapter.updateData(updatedList)
        }
    }

    private fun moveToChatFragment(carNum1: String, carNum2: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChatFragment.newInstance(carNum1, carNum2))
            .addToBackStack(null)
            .commit()
    }
}
