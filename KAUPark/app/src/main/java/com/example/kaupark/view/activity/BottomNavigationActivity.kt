package com.example.kaupark.view.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kaupark.R
import com.example.kaupark.databinding.ActivityBottomNavigationBinding
import com.example.kaupark.view.fragment.ChattingListFragment
import com.example.kaupark.view.fragment.HomeFragment
import com.example.kaupark.view.fragment.ParkingAvailableFragment
import com.example.kaupark.view.fragment.ParkingPaymentFragment

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
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment()).commit()
                    true
                }
                R.id.fragment_park -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, ParkingAvailableFragment()).commit()
                    true
                }
                R.id.fragment_payment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, ParkingPaymentFragment()).commit()
                    true
                }
                R.id.fragment_chat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, ChattingListFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
}