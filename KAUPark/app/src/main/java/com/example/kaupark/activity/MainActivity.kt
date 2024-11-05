package com.example.kaupark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kaupark.databinding.ChattingListBinding
import com.example.kaupark.fragment.ChattingList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ChattingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChattingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, ChattingList()) // android.R.id.content로 최상위 뷰에 추가
                .commit()
        }
    }
}
