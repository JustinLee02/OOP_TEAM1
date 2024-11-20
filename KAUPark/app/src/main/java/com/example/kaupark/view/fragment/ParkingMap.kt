package com.example.kaupark.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentParkingMapBinding
import com.example.kaupark.databinding.FragmentParkingMapSubBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class ParkingMap : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentParkingMapBinding

    val marker1 = Marker() // 과학관 주차장
    val marker2 = Marker() // 운동장 주차장
    val marker3 = Marker() // 학생회관 주차장
    val marker4 = Marker() // 연구동 주차장
    val marker5 = Marker() // 도서관 주차장
    val marker6 = Marker() // 산학협력관 주차장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParkingMapBinding.inflate(inflater, container, false)

        binding.maptoolbar.apply {
            setNavigationIcon(R.drawable.arrow_back)
            setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapmain) as? MapFragment
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().replace(R.id.mapmain, it).commit()
            } // 지도 객체 생성
        mapFragment.getMapAsync(this) // async로 지도 초기화

        return binding.root
    }

    override fun onMapReady(naverMap: NaverMap) {
        val initialPosition = LatLng(37.6000000, 126.8656335) // 위도 경도 지정
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)

        naverMap.moveCamera(cameraUpdate)
        val zoomUpdate = CameraUpdate.zoomTo(15.8) // Zoom 레벨 설정
        naverMap.moveCamera(zoomUpdate)

        val infoWindow = InfoWindow() // 정보창 객체
        infoWindow.adapter = object: InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return infoWindow.marker?.tag as CharSequence? ?: "" // 정보창에 tag 값을 표시하는 어댑터
            }
        }

        marker1.position = LatLng(37.6020819, 126.8657351)
        marker1.captionText = "과학관 주차장"
        marker1.tag = "여유"
        marker1.isHideCollidedSymbols = true // Hiding 겹치는 심볼
        marker1.map = naverMap
        marker1.icon = MarkerIcons.RED
        marker1.width = 50
        marker1.height = 80

        marker1.setOnClickListener {
            infoWindow.open(marker1)

            val subFragment = ParkingMapSubFragment.newInstance(marker1.captionText.toString())
                parentFragmentManager.beginTransaction()
                .replace(R.id.subFrag, subFragment)
                .commit()

            true
        }

        marker2.position = LatLng(37.6009608, 126.8658735)
        marker2.captionText = "운동장 옆 주차장"
        marker2.tag = "운동장 옆 주차장"
        marker2.isHideCollidedSymbols = true
        marker2.map = naverMap
        marker2.icon = MarkerIcons.RED
        marker2.width = 50
        marker2.height = 80
        marker2.setOnClickListener {
            infoWindow.open(marker2)
            val subFragment = ParkingMapSubFragment.newInstance(marker2.captionText.toString())
                parentFragmentManager.beginTransaction()
                .replace(R.id.subFrag, subFragment)
                    .commit()
            true
        }

        marker3.position = LatLng(37.6001840, 126.8644001)
        marker3.captionText = "학생회관 주차장"
        marker3.tag = "학생회관 주차장"
        marker3.isHideCollidedSymbols = true
        marker3.map = naverMap
        marker3.icon = MarkerIcons.RED
        marker3.width = 50
        marker3.height = 80
        marker3.setOnClickListener {
            infoWindow.open(marker3)
            val subFragment = ParkingMapSubFragment.newInstance(marker3.captionText.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.subFrag, subFragment)
                .commit()
            true
        }

        marker4.position = LatLng(37.5977161, 126.8645011)
        marker4.captionText = "연구동 주차장"
        marker4.tag = "연구동 주차장"
        marker4.isHideCollidedSymbols = true
        marker4.map = naverMap
        marker4.icon = MarkerIcons.RED
        marker4.width = 50
        marker4.height = 80
        marker4.setOnClickListener {
            infoWindow.open(marker4)
            val subFragment = ParkingMapSubFragment.newInstance(marker4.captionText.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.subFrag, subFragment)
                .commit()
            true
        }

        marker5.position = LatLng(37.5984458, 126.8641054)
        marker5.captionText = "도서관 주차장"
        marker5.tag = "도서관 주차장"
        marker5.isHideCollidedSymbols = true
        marker5.map = naverMap
        marker5.icon = MarkerIcons.RED
        marker5.width = 50
        marker5.height = 80
        marker5.setOnClickListener {
            infoWindow.open(marker5)
            val subFragment = ParkingMapSubFragment.newInstance(marker5.captionText.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.subFrag, subFragment)
                .commit()
            true
        }

        marker6.position = LatLng(37.5981439, 126.8653157)
        marker6.captionText = "산학협력관 주차장"
        marker6.tag = "산학협력관 주차장"
        marker6.isHideCollidedSymbols = true
        marker6.map = naverMap
        marker6.icon = MarkerIcons.RED
        marker6.width = 50
        marker6.height = 80
        marker6.setOnClickListener {
            infoWindow.open(marker6)
            val subFragment = ParkingMapSubFragment.newInstance(marker6.captionText.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.subFrag, subFragment)
                .commit()
            true
        }
    }

    fun markerGetClicked(marker: Marker) {
        if (marker.captionText == "과학관 주차장") {
            
        }
    }

}