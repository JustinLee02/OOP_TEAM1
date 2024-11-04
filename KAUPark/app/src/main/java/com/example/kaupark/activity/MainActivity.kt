package com.example.kaupark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.ChattingListBinding
import com.example.kaupark.fragment.ChatPopupFragment
import com.example.kaupark.model.Person

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

        // 스와이프하여 삭제하는 기능 추가
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // 이동 기능은 필요 없음
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                adapter.removePerson(position) // 아이템 삭제
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recChatting)
    }

    override fun onPersonAdded(person: Person) {
        adapter.addPerson(person)
    }
}
