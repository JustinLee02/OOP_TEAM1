package com.example.kaupark.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kaupark.R
import com.example.kaupark.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.signinbutton.setOnClickListener {
//            val intent = Intent(this, BottomNavigationActivity::class.java)
//            startActivity(intent)
//        }

        binding.signupbutton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        val loginButton: Button = findViewById(R.id.signinbutton)

        loginButton.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.idtextfield).text.toString()
        val password = findViewById<EditText>(R.id.pwtextfield).text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    navigateToHomeActivity()
                } else {
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, BottomNavigationActivity::class.java)
        startActivity(intent)
        finish()
    }
}