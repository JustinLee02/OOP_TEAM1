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

    private val viewModel: ChatPopupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChatPopupBinding.inflate(inflater, container, false)

        binding.buttonSendpopup.setOnClickListener {
            lifecycleScope.launch {
                val carNum2 = binding.edittextBlank.text.toString()

                try {
                    val carNum1 = viewModel.getCurrentUserCarNum()

                    if (carNum1.isEmpty()) {
                        Toast.makeText(context, "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val doesCarNumExist = viewModel.doesCarNumExist(carNum2)
                    if (!doesCarNumExist) {
                        Toast.makeText(context, "존재하지 않는 차량 번호입니다.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val doesChatExist = viewModel.doesChatExist(carNum1, carNum2)
                    if (doesChatExist) {
                        moveToChatFragment(carNum1, carNum2)
                    } else {
                        viewModel.createChat(carNum1, carNum2)
                        moveToChatFragment(carNum1, carNum2)
                    }

                    binding.edittextBlank.text.clear()
                    dismiss()

                } catch (e: Exception) {
                    Log.e("ChatPopupFragment", "오류 발생: ${e.message}", e)
                }
            }
        }

        binding.buttonClosepopup.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun moveToChatFragment(carNum1: String, carNum2: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChatFragment.newInstance(carNum1, carNum2))
            .addToBackStack(null)
            .commit()
    }
}
