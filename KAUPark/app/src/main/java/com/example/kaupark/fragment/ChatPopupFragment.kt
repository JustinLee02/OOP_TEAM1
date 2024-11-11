package com.example.kaupark.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.kaupark.model.Person
import com.example.kaupark.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatPopupFragment : DialogFragment() {

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val view = inflater.inflate(R.layout.fragment_chat_popup, container, false)

        val sendButton: Button = view.findViewById(R.id.send_btn)
        val closeButton: Button = view.findViewById(R.id.close_btn)
        val nameEditText: EditText = view.findViewById(R.id.blank_text)


        sendButton.setOnClickListener {
            var person = Person()
            person.carNum = nameEditText.text.toString()
            person.currentTime = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())


            firestore?.collection("chattingList")?.document()?.set(person)


            nameEditText.text.clear()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ChatFragment())
                .addToBackStack(null) // 뒤로 가기 버튼을 누르면 이전 Fragment로 돌아가게 함
                .commit()
            dismiss()
        }

        closeButton.setOnClickListener{
            dismiss()
        }

        return view
    }
}
