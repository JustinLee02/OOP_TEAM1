package com.example.kaupark.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kaupark.R
import com.example.kaupark.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Firebase 사용하는 권한
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.signupbuttonfinal.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }

        // 회원가입 부분
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val signupButton: Button = findViewById(R.id.signupbuttonfinal)
        signupButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val id = findViewById<EditText>(R.id.nametextfield).text.toString()
        val studentId = findViewById<EditText>(R.id.studentidtextfield).text.toString()
        val password = findViewById<EditText>(R.id.passwordtextfield).text.toString()
        val phoneNum = findViewById<EditText>(R.id.phonenumtextfield).text.toString()
        val email = findViewById<EditText>(R.id.emailtextfield).text.toString()
        val carNum = findViewById<EditText>(R.id.carnumtextfield).text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if(task.isSuccessful) {
                val user = auth.currentUser
                saveUserData(id, studentId, password, phoneNum, email, carNum)
                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                navigateToLoginActivity()
            } else {
                Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(id: String, studentId: String, password: String, phoneNum: String, email: String, carNum: String) {
        val user = hashMapOf(
            "id" to id,
            "studentId" to studentId,
            "password" to password,
            "phoneNum" to phoneNum,
            "email" to email,
            "carNum" to carNum
        )

        firestore.collection("users")
            .document(id)
            .set(user)
            .addOnSuccessListener { document ->
                Log.d("SignupActivity", "DocumentSnapshot added with ID: $id")
            }
            .addOnFailureListener { e ->
                Log.e("SignupActivity", "문서 추가 오류", e)
            }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}