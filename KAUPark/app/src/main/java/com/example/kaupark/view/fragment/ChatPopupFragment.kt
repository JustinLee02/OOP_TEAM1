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
    ): View {
        val binding = FragmentChatPopupBinding.inflate(inflater, container, false)


        binding.sendBtn.setOnClickListener {
            lifecycleScope.launch { // 코루틴 내에서 비동기 작업 처리, 하단에 await()를 사용하기 위해 코루틴 블럭을 만들어 준다고 생각하면 될 듯
                try {
                    val userId = auth.currentUser?.uid ?: return@launch


//                     * Firestore에서 사용자 정보 가져오기
//                     * - Firestore는 비동기 작업
//                     * - get()으로 데이터를 가져온 후, await()를 사용하여 작업 완료를 대기
//                     * - 현재 코루틴 내에서만 동작


                    val document = firestore.collection("users").document(userId).get().await() // document 반환되기 전까지 다음줄로 진행 X, 코루틴 내부에서만 사용할 수 있음
                    val carNum = document.getString("carNum") ?: "차량정보 없삼"

                    // Person 객체 생성 및 데이터 설정
                    val person = Person().apply {
                        participants[0] = carNum
                        participants[1] = binding.blankText.text.toString()
                        currentTime = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(Date())
                    }

                    // get()과 마찬가지로 비동기작업, awaiat()를 사용하여 작업 완료 대기
                    firestore.collection("chattingLists").document().set(person).await() // Firestore 저장작업 완료까지 다음줄로 진행 X

                    binding.blankText.text.clear()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main_container, ChatFragment())
                        .addToBackStack(null) // 뒤로 가기 버튼을 누르면 이전 Fragment로 돌아가게 함
                        .commit()
                    dismiss()

                } catch (e: Exception){
                    Log.e("Error", "오류 발생 ${e.message}", e)
                }
            }



        }

        binding.closeBtn.setOnClickListener{
            dismiss()
        }

        return binding.root
    }

}
