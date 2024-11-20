package com.example.kaupark.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.kaupark.R
import com.example.kaupark.data.ParkingClass
import com.example.kaupark.databinding.FragmentParkingMapBinding
import com.example.kaupark.databinding.FragmentParkingMapSubBinding
import com.example.kaupark.viewmodel.HomeViewModel
import com.example.kaupark.viewmodel.ParkingMapViewModel
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

    private val viewModel: ParkingMapViewModel by viewModels(
    )

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

        val infoWindow = InfoWindow().apply {
            adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return infoWindow.marker?.tag as? CharSequence ?: "정보 없음"
                }
            }
        }

        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            markers.forEach { marker ->
                marker.map = naverMap
                marker.setOnClickListener {
                    if (infoWindow.marker == marker) {
                        infoWindow.close() // 같은 마커를 클릭하면 닫기
                    } else {
                        infoWindow.open(marker) // InfoWindow 열기
                    }
                    viewModel.selectMarker(marker)
                    true
                }
            }
        }

        viewModel.selectedParkingLot.observe(viewLifecycleOwner) { parkingLot ->
            val subFragment = ParkingMapSubFragment.newInstance(parkingLot)
            parentFragmentManager.beginTransaction()
                .replace(R.id.subFrag, subFragment)
                .commit()
        }
    }
}
