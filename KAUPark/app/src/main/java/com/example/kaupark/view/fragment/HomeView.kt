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

/**
 * HomeView Fragment
 * Description: 메인 화면 역할을 하는 Fragment로, 주차장 정보를 보여주고 지도와 사용자 상호작용을 처리.
 * Implements OnMapReadyCallback: 네이버 맵 초기화 및 마커를 관리.
 */

class HomeView : Fragment(), OnMapReadyCallback {

    private lateinit var binding: HomeViewBinding

    // 현재 날짜를 저장하는 변수 (API 26 이상 필요)
    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDate = LocalDate.now()

    // HomeViewModel 을 Fragment 에 연결. ViewModel 은 데이터를 관리하고 UI로 동기화
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

        // 현재 날짜를 홈 화면에 표시
        binding.textviewCurrenttime.text = date.toString()

        // 지도를 초기화하고 Fragment와 연결
        val mapFragment = childFragmentManager.findFragmentById(R.id.containerview_mapimage) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.containerview_mapimage, it).commit()
            }
        mapFragment.getMapAsync(this)

        // buttonMangeProfile 버튼 클릭 시 ManageProfile Fragment 로 이동
        binding.buttonManageprofile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ManageProfile())
                .addToBackStack(null)
                .commit()
        }

        // 주차장 리스트 Spinner (드롭다운 메뉴) 설정
        val parkingLots = listOf(
            "과학관 주차장",
            "운동장 옆 주차장",
            "학생회관 주차장",
            "도서관 주차장",
            "연구동 주차장",
            "산학협력관 주차장"
        )

        // ArrayAdapter 를 사용해 Spinner 에 주차장 리스트를 설정.
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, parkingLots).also { arrayAdapter ->  
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerParkinglot.adapter = adapter

        // 입차 버튼 클릭 이벤트 처리.
        binding.buttonIn.setOnClickListener {
            val parkingLot = binding.spinnerParkinglot.selectedItem?.toString()

            if (!parkingLot.isNullOrBlank()) {
                viewModel.increaseCarNum(parkingLot)
                viewModel.recordEntryTime()
            } else {
                ToastHelper.showToast(requireContext(), "주차장을 선택하세요")
            }
        }

        // 출차 버튼 클릭 이벤트 처리.
        binding.buttonOut.setOnClickListener {
            val parkingLot = binding.spinnerParkinglot.selectedItem?.toString()
            if(!parkingLot.isNullOrBlank()) {
                viewModel.decreaseCarNum(parkingLot)
            } else {
                ToastHelper.showToast(requireContext(), "주차장 이름을 입력하세요")
            }
            viewModel.recordExitTime()
        }

        // ViewModel의 LiveData 관찰을 통해 UI를 업데이트.
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

    /**
     * OnMapReadyCallback 구현.
     * 네이버 맵이 준비되었을 때 호출되는 메서드.
     */
    override fun onMapReady(naverMap: NaverMap) {
        viewModel.markers.observe(viewLifecycleOwner) { markerList ->
            markerList.forEach { marker ->
                marker.map = naverMap // 지도에 마커 추가.
                marker.setOnClickListener {
                    viewModel.selectMarker(marker) // 클릭된 마커 선택.
                    true
                }
            }
        }

        // InfoWindow 설정 (마커 클릭 시 표시되는 정보창).
        val infoWindow = InfoWindow().apply {
            adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return infoWindow.marker?.tag as? CharSequence ?: "정보 없음"
                }
            }
        }

        // InfoWindow를 마커 클릭 이벤트에 연결.
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

        // 지도 초기 위치 및 줌 레벨 설정.
        val initialPosition = LatLng(37.6000000, 126.8656335) // 위도 경도 지정
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
        naverMap.moveCamera(cameraUpdate)
        val zoomUpdate = CameraUpdate.zoomTo(15.8) // Zoom 레벨 설정
        naverMap.moveCamera(zoomUpdate)
    }

}