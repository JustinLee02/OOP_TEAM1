package com.example.kaupark.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentChatPopupBinding
import com.example.kaupark.viewmodel.ChatPopupViewModel
import kotlinx.coroutines.launch

class ChatPopupFragment : DialogFragment() {

    // ViewModel을 생성하여 데이터와 로직을 관리함
    private val viewModel: ChatPopupViewModel by viewModels()

    // Fragment가 생성될 때 호출되며, 화면을 구성함
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Fragment의 레이아웃을 바인딩
        val binding = FragmentChatPopupBinding.inflate(inflater, container, false)

        // "전송" 버튼 클릭 이벤트 처리
        binding.buttonSendpopup.setOnClickListener {
            // Coroutine을 사용하여 비동기 작업 실행
            lifecycleScope.launch {
                val carNum2 = binding.edittextBlank.text.toString() // 입력한 차량 번호 가져오기

                try {
                    val carNum1 = viewModel.getCurrentUserCarNum() // 현재 사용자의 차량 번호 가져오기

                    // 차량 번호를 불러오지 못한 경우 처리
                    if (carNum1.isEmpty()) {
                        Toast.makeText(context, "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // 입력한 차량 번호가 존재하는지 확인
                    val doesCarNumExist = viewModel.doesCarNumExist(carNum2)
                    if (!doesCarNumExist) {
                        Toast.makeText(context, "존재하지 않는 차량 번호입니다.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // 두 차량 번호 간의 채팅방이 이미 존재하는지 확인
                    val doesChatExist = viewModel.doesChatExist(carNum1, carNum2)
                    if (doesChatExist) {
                        moveToChatFragment(carNum1, carNum2) // 기존 채팅방으로 이동
                    } else {
                        viewModel.createChat(carNum1, carNum2) // 새 채팅방 생성
                        moveToChatFragment(carNum1, carNum2) // 새로 생성한 채팅방으로 이동
                    }

                    binding.edittextBlank.text.clear() // 입력 필드 초기화
                    dismiss() // 팝업 창 닫기

                } catch (e: Exception) {
                    Log.e("ChatPopupFragment", "오류 발생: ${e.message}", e) // 오류 로그 출력
                }
            }
        }

        // "닫기" 버튼 클릭 시 팝업 닫기
        binding.buttonClosepopup.setOnClickListener {
            dismiss()
        }

        return binding.root // 바인딩된 레이아웃 반환
    }

    // 채팅 화면으로 이동
    private fun moveToChatFragment(carNum1: String, carNum2: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChatFragment.newInstance(carNum1, carNum2)) // ChatFragment로 화면 교체
            .addToBackStack(null) // 뒤로 가기 시 이전 화면으로 돌아가기 가능
            .commit() // 트랜잭션 실행
    }
}
