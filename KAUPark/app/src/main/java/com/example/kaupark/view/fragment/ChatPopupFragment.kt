package com.example.kaupark.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.kaupark.model.Person
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentChatPopupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatPopupFragment : DialogFragment() {

    var auth : FirebaseAuth = Firebase.auth
    var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatPopupBinding.inflate(inflater, container, false)


        binding.sendBtn.setOnClickListener {
            var person = Person()

            val userId = auth.currentUser?.uid ?: return@setOnClickListener
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    person.participants[0] = document.getString("carNum") ?: "차량정보 없삼"
                }
                .addOnFailureListener{
                    Log.d("실패","실패")
                }
            person.participants[1] = binding.blankText.text.toString()
            person.currentTime = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())


            firestore.collection("chattingLists").document().set(person)


            binding.blankText.text.clear()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ChatFragment())
                .addToBackStack(null) // 뒤로 가기 버튼을 누르면 이전 Fragment로 돌아가게 함
                .commit()
            dismiss()
        }

        binding.closeBtn.setOnClickListener{
            dismiss()
        }

        return binding.root
    }

}
