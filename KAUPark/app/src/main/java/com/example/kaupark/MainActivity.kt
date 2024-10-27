package com.example.kaupark

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.kaupark.databinding.ActivityMainBinding
import com.example.kaupark.databinding.ParkingAvailableBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ParkingAvailableBinding.inflate(layoutInflater)
        val tr = supportFragmentManager.beginTransaction().run {
            replace(binding.parkingAvailable.id, ParkingAvailable())
            commit()
        }

        setContentView(binding.root)
    }
}