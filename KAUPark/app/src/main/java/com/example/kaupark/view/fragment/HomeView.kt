package com.example.kaupark.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.kaupark.R
import com.example.kaupark.ToastHelper
import com.example.kaupark.databinding.HomeViewBinding
import com.example.kaupark.viewmodel.HomeViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.InfoWindow
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
        binding.textviewCurrenttime.text = date.toString()

        //
        val mapFragment = childFragmentManager.findFragmentById(R.id.containerview_mapimage) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.containerview_mapimage, it).commit()
            }
        mapFragment.getMapAsync(this)

        // Display ParkingMap Fragment
//        binding.buttonTransparent.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.main_container, ParkingMap())
//                .addToBackStack(null)
//                .commit()
//        }

        binding.buttonManageprofile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ManageProfile())
                .addToBackStack(null)
                .commit()
        }

        val parkingLots = listOf(
            "과학관 주차장",
            "운동장 옆 주차장",
            "학생회관 주차장",
            "도서관 주차장",
            "연구동 주차장",
            "산학협력관 주차장"
        )

        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, parkingLots).also { arrayAdapter ->  
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerParkinglot.adapter = adapter

        binding.buttonIn.setOnClickListener {
            val parkingLot = binding.spinnerParkinglot.selectedItem?.toString()

            if (!parkingLot.isNullOrBlank()) {
                viewModel.increaseCarNum(parkingLot)
                viewModel.recordEntryTime()
            } else {
                ToastHelper.showToast(requireContext(), "주차장을 선택하세요")
            }
        }

        binding.buttonOut.setOnClickListener {
            val parkingLot = binding.spinnerParkinglot.selectedItem?.toString()
            if(!parkingLot.isNullOrBlank()) {
                viewModel.decreaseCarNum(parkingLot)
            } else {
                ToastHelper.showToast(requireContext(), "주차장 이름을 입력하세요")
            }
            viewModel.recordExitTime()
        }
        
        viewModel.userCarNum.observe(viewLifecycleOwner) { carNum ->
            binding.textviewUsercarnum.text = carNum
        }

        viewModel.parkingFee.observe(viewLifecycleOwner) { fee ->
            binding.textviewParkingfee.text = fee
        }

        viewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.textviewMyinfo.text = "${userName} 님"
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                // Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                ToastHelper.showToast(requireContext(), it)
            }
        }

        viewModel.fetchUserInfo()

        return binding.root
    }

    override fun onMapReady(naverMap: NaverMap) {
        viewModel.markers.observe(viewLifecycleOwner) { markerList ->
            markerList.forEach { marker ->
                marker.map = naverMap
                marker.setOnClickListener {
                    viewModel.selectMarker(marker)
                    // ToastHelper.showToast(requireContext(), marker.tag.toString())
                    true
                }
            }
        }

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

        val initialPosition = LatLng(37.6000000, 126.8656335) // 위도 경도 지정
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        naverMap.moveCamera(cameraUpdate)
        val zoomUpdate = CameraUpdate.zoomTo(15.8) // Zoom 레벨 설정
        naverMap.moveCamera(zoomUpdate)
    }

}