package com.example.kaupark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaupark.databinding.ChattingListBinding

class MainActivity : AppCompatActivity(), ChatPopupFragment.OnPersonAddedListener {

    private lateinit var binding: ChattingListBinding
    private val personList = mutableListOf(
        Person("4130", "새로운 메세지가 왔습니다.", "pm 23:59"),
        Person("9997", "새로운 메세지가 왔습니다.", "pm 22:00"),
        Person("1234", "새로운 메세지가 왔습니다.", "pm 13:30"),
        Person("1111", "새로운 메세지가 왔습니다.", "am 11:00"),
        Person("1100", "새로운 메세지가 왔습니다.", "am 9:47")
    )
    private lateinit var adapter: PersonsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChattingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PersonsAdapter(personList)
        binding.recChatting.layoutManager = LinearLayoutManager(this)
        binding.recChatting.adapter = adapter

        binding.chatplusBtn.setOnClickListener {
            val chatPopupFragment = ChatPopupFragment()
            chatPopupFragment.show(supportFragmentManager, "ChatPopupFragment")
        }
    }

    override fun onPersonAdded(person: Person) {
        adapter.addPerson(person)
    }
}
