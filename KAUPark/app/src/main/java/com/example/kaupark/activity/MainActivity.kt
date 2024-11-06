package com.example.kaupark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kaupark.databinding.ChattingListBinding
import com.example.kaupark.fragment.ChattingList
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ChattingListBinding
    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        uid = FirebaseAuth.getInstance().currentUser?.uid
        firestore = FirebaseFirestore.getInstance()

        binding = ChattingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, ChattingList())
                .commit()
        }
    }
}
