package com.example.kaupark.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.kaupark.model.Person
import com.example.kaupark.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatPopupFragment : DialogFragment() {

    interface OnPersonAddedListener {
        fun onPersonAdded(person: Person)
    }

    private var listener: OnPersonAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as? OnPersonAddedListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_popup, container, false)

        val sendButton: Button = view.findViewById(R.id.send_btn)
        val closeButton: Button = view.findViewById(R.id.close_btn)
        val nameEditText: EditText = view.findViewById(R.id.blank_text)

        sendButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val currentTime = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())

            if (name.isNotBlank()) {
                listener?.onPersonAdded(Person("$name", "채팅을 시작하세요.", currentTime))
                dismiss()
            }
        }

        closeButton.setOnClickListener{
            dismiss()
        }




        return view
    }
}