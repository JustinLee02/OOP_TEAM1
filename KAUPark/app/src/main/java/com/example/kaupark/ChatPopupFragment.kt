package com.example.kaupark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class ChatPopupFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this dialog
        val view = inflater.inflate(R.layout.fragment_chat_popup, container, false)

        // 닫기 버튼 클릭 리스너
        val closeButton: Button = view.findViewById(R.id.close_btn)
        closeButton.setOnClickListener {
            dismiss() // DialogFragment 닫기
        }

        return view
    }
}