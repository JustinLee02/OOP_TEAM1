package com.example.kaupark.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.FragmentChattingListBinding
import com.example.kaupark.view.adapter.PersonsAdapter
import com.example.kaupark.R
import com.example.kaupark.viewmodel.ChattingListViewModel
import kotlinx.coroutines.launch

class ChattingListFragment : Fragment() {

    // ViewModel 초기화: 데이터를 관리하고 LiveData를 통해 UI와 동기화
    private val viewModel: ChattingListViewModel by viewModels()

    // View 바인딩 객체 선언
    private lateinit var binding: FragmentChattingListBinding

    // RecyclerView 어댑터 선언
    private lateinit var adapter: PersonsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 레이아웃 XML 파일과 연결하여 View 바인딩 객체 생성
        binding = FragmentChattingListBinding.inflate(inflater, container, false)

        // RecyclerView 초기 설정
        setupRecyclerView()

        // LiveData 관찰 설정
        setupObservers()

        // 사용자 차량 번호를 ViewModel에서 로드
        lifecycleScope.launch {
            viewModel.loadMyCarNum()
        }

        // "+" 버튼 클릭 시 새로운 채팅 추가 팝업 표시
        binding.chatplusBtn.setOnClickListener {
            val chatPopupFragment = ChatPopupFragment()
            chatPopupFragment.show(parentFragmentManager, "ChatPopupFragment")
        }

        // RecyclerView 스와이프 동작 추가: 왼쪽 스와이프로 항목 삭제
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            // 스와이프된 항목에 대한 삭제 동작 처리
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val person = viewModel.personList.value?.get(position) ?: return

                // 현재 사용자의 차량 번호 가져오기
                val carNum = viewModel.carNum.value ?: return

                // 상대방의 차량 번호를 식별
                val receiver = if (person.participants[0] == carNum) person.participants[1] else person.participants[0]

                lifecycleScope.launch {
                    // Firestore에서 채팅 목록 삭제
                    viewModel.deleteChattingList(person, receiver)
                    // 로컬 목록에서 항목 삭제
                    viewModel.removePerson(position)
                }
            }
        })

        // ItemTouchHelper를 RecyclerView에 연결
        itemTouchHelper.attachToRecyclerView(binding.recChatting)

        return binding.root
    }

    // RecyclerView 설정 함수
    private fun setupRecyclerView() {
        // RecyclerView의 레이아웃 매니저 설정
        binding.recChatting.layoutManager = LinearLayoutManager(context)

        // 어댑터 초기화 및 설정
        adapter = PersonsAdapter(mutableListOf(), "") { person ->
            // 채팅 목록 클릭 시 ChatFragment로 이동
            val carNum = viewModel.carNum.value ?: ""
            val receiver = if (person.participants[0] == carNum) person.participants[1] else person.participants[0]
            moveToChatFragment(carNum, receiver)
        }
        binding.recChatting.adapter = adapter
    }

    // LiveData 관찰 설정 함수
    private fun setupObservers() {
        // 사용자의 차량 번호(carNum) 관찰
        viewModel.carNum.observe(viewLifecycleOwner) { carNum ->
            // 차량 번호가 업데이트되면 어댑터 재설정
            adapter = PersonsAdapter(mutableListOf(), carNum) { person ->
                val receiver = if (person.participants[0] == carNum) person.participants[1] else person.participants[0]
                moveToChatFragment(carNum, receiver)
            }
            binding.recChatting.adapter = adapter

            // 채팅 목록 데이터를 ViewModel에서 가져오기
            lifecycleScope.launch {
                viewModel.fetchChattingList()
            }
        }

        // 채팅 상대 목록(personList) 관찰
        viewModel.personList.observe(viewLifecycleOwner) { updatedList ->
            // 목록 데이터가 변경되면 어댑터 업데이트
            adapter.updateData(updatedList)
        }
    }

    // ChatFragment로 이동하는 함수
    private fun moveToChatFragment(carNum1: String, carNum2: String) {
        // ChatFragment로 전환하며 BackStack에 추가
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChatFragment.newInstance(carNum1, carNum2))
            .addToBackStack(null)
            .commit()
    }
}
