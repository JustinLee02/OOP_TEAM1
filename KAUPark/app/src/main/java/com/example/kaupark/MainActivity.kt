package com.example.kaupark

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.kaupark.databinding.ActivityMainBinding
import com.example.kaupark.databinding.ChattingListBinding
import com.example.kaupark.databinding.ParkingAvailableBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    val p = arrayOf(
        Person("4130님", "새로운 메세지가 왔습니다.", "pm 23:59"),
        Person("9997님", "새로운 메세지가 왔습니다.", "pm 22:00"),
        Person("1234님", "새로운 메세지가 왔습니다.", "pm 13:30"),
        Person("1111님", "새로운 메세지가 왔습니다.", "am 11:00"),
        Person("1100님", "새로운 메세지가 왔습니다.", "am 9:47")
    )

    lateinit var binding : ChattingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ChattingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recChatting.layoutManager = LinearLayoutManager(this)
        binding.recChatting.adapter = PersonsAdapter(p)

    }
}