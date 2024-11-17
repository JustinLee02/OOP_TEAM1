package com.example.kaupark.view.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.kaupark.R
import com.example.kaupark.databinding.HomeViewBinding
import com.example.kaupark.viewmodel.HomeViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.MapFragment
import java.time.LocalDate

class HomeView : Fragment(), OnMapReadyCallback {

    private lateinit var binding: HomeViewBinding

    // Checking current time
    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDate = LocalDate.now()

    // Declare viewModel
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // View Binding
        binding = HomeViewBinding.inflate(inflater, container, false)

        // Display current time on id: "timetext"
        binding.timetext.text = date.toString()

        //
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapimage) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.mapimage, it).commit()
            }
        mapFragment.getMapAsync(this)

        // Display ParkingMap Fragment
        binding.transparentbutton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ParkingMap())
                .addToBackStack(null)
                .commit()
        }

        binding.inbutton.setOnClickListener {
            Log.d("HomeView", "In Cliked") // Test log
            viewModel.recordEntryTime()
        }

        binding.outbutton.setOnClickListener {
            Log.d("HomeView", "Out Cliked") // Test log

            viewModel.recordExitTime()
        }
        
        viewModel.userCarNum.observe(viewLifecycleOwner) { carNum ->
            binding.usercarnum.text = carNum
        }

        viewModel.parkingFee.observe(viewLifecycleOwner) { fee ->
            binding.parkingfee.text = fee
        }

        viewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.myInfo.text = "${userName} 님"
        }

        viewModel.fetchUserInfo()

        return binding.root
    }

    override fun onMapReady(naverMap: NaverMap) {
        val initialPosition = LatLng(37.5997399, 126.8644887)
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)

        naverMap.moveCamera(cameraUpdate)
        val zoomUpdate = CameraUpdate.zoomTo(16.0)
        naverMap.moveCamera(zoomUpdate)
    }

}