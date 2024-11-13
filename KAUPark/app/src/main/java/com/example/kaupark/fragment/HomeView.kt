package com.example.kaupark.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kaupark.R
import com.example.kaupark.databinding.HomeViewBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.MapFragment

class HomeView : Fragment(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeViewBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapimage) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.mapimage, it).commit()
            }
        mapFragment.getMapAsync(this)

        binding.inbutton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ParkingMap())
                .addToBackStack(null)
                .commit()
        }
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