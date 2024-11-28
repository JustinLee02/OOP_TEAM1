package com.example.kaupark.view.activity


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kaupark.R
import com.example.kaupark.databinding.ActivityBottomNavigationBinding
import com.example.kaupark.view.fragment.ChattingList
import com.example.kaupark.view.fragment.HomeView
import com.example.kaupark.view.fragment.ParkingAvailable
import com.example.kaupark.view.fragment.ParkingPayment

class BottomNavigationActivity : AppCompatActivity() {
    private val binding: ActivityBottomNavigationBinding by lazy {
        ActivityBottomNavigationBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNav()

        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_home
        }

    }

    fun setupBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.fragment_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeView()).commit()
                    true
                }
                R.id.fragment_park -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, ParkingAvailable()).commit()
                    true
                }
                R.id.fragment_payment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, ParkingPayment()).commit()
                    true
                }
                R.id.fragment_chat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, ChattingList()).commit()
                    true
                }
                else -> false
            }
        }
    }
}