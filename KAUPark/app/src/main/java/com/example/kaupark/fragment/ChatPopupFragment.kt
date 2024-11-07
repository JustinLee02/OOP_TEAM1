package com.example.kaupark.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.kaupark.model.Person
import com.example.kaupark.R
import com.example.kaupark.model.ResultDTO
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
        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val view = inflater.inflate(R.layout.fragment_chat_popup, container, false)

        val sendButton: Button = view.findViewById(R.id.send_btn)
        val closeButton: Button = view.findViewById(R.id.close_btn)
        val nameEditText: EditText = view.findViewById(R.id.blank_text)


        sendButton.setOnClickListener {
            var resultDTO = ResultDTO()

            resultDTO.carNum = nameEditText.text.toString()
            resultDTO.currentTime = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())


            firestore?.collection("chattingList")?.document()?.set(resultDTO)
//
//            firestore?.collection("chattingList")
//                ?.addSnapshotListener { snapshot, e ->
//                    if (e != null) {
//                        Log.w("ChatPopupFragment", "Listen failed.", e)
//                        return@addSnapshotListener
//                    }
//
//                    if (snapshot != null) {
//                        // snapshot을 통해 채팅 목록 데이터 가져오기
//                        val chatList = snapshot.documents.mapNotNull { it.toObject(ResultDTO::class.java) }
//
//                        // 가져온 데이터를 listener를 통해 전달하여 화면에 업데이트
//                        chatList.forEach { resultDTO ->
//                            listener?.onPersonAdded(Person("${resultDTO.carNum}님", "채팅을 시작하세요.", resultDTO.currentTime ?: ""))
//                        }
//                    }
//                }

            nameEditText.text.clear()

//            if (!resultDTO.carNum.isNullOrBlank()) {
//                listener?.onPersonAdded(Person("${resultDTO.carNum}님", "채팅을 시작하세요.", resultDTO.currentTime!!))
//                dismiss()
//            }
        }

        closeButton.setOnClickListener{
            dismiss()
        }

        return view
    }
}