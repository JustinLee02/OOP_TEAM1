package com.example.kaupark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kaupark.databinding.ChattingListBinding
import com.example.kaupark.fragment.ChattingList
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ChattingListBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        binding = ChattingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, ChattingList())
                .commit()
        }
    }
}
