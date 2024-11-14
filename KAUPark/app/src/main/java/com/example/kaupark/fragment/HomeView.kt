package com.example.kaupark.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kaupark.R
import com.example.kaupark.databinding.HomeViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.MapFragment

class HomeView : Fragment(), OnMapReadyCallback {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: HomeViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding = HomeViewBinding.inflate(inflater, container, false)

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

        fetchUserInfo()

        return binding.root
    }

    override fun onMapReady(naverMap: NaverMap) {
        val initialPosition = LatLng(37.5997399, 126.8644887)
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)

        naverMap.moveCamera(cameraUpdate)
        val zoomUpdate = CameraUpdate.zoomTo(16.0)
        naverMap.moveCamera(zoomUpdate)
    }

    private fun fetchUserInfo() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if(document != null) {
                    val carNum = document.getString("carNum") ?: "차량 번호 없음"
                    val name = document.getString("name") ?: "이름 없음"

                    binding.usercarnum.text = carNum
                } else {
                    // Toast.makeText(this, "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Toast.makeText(this, "실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}