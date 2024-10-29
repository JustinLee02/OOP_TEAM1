package com.example.kaupark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaupark.databinding.ChattingListBinding

class MainActivity : AppCompatActivity() {

    val p = arrayOf(
        Person("4130님", "새로운 메세지가 왔습니다.", "pm 23:59"),
        Person("9997님", "새로운 메세지가 왔습니다.", "pm 22:00"),
        Person("1234님", "새로운 메세지가 왔습니다.", "pm 13:30"),
        Person("1111님", "새로운 메세지가 왔습니다.", "am 11:00"),
        Person("1100님", "새로운 메세지가 왔습니다.", "am 9:47")
    )

    lateinit var binding: ChattingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChattingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recChatting.layoutManager = LinearLayoutManager(this)
        binding.recChatting.adapter = PersonsAdapter(p)

        // chatplus_btn 클릭 리스너 추가
        binding.chatplusBtn.setOnClickListener {
            val chatPopupFragment = ChatPopupFragment()
            chatPopupFragment.show(supportFragmentManager, "ChatPopupFragment")
        }
    }
}