package com.example.kaupark.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.kaupark.R
import com.example.kaupark.databinding.HomeViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.MapFragment
import java.time.LocalDate

class HomeView : Fragment(), OnMapReadyCallback {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: HomeViewBinding

    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDate = LocalDate.now()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding = HomeViewBinding.inflate(inflater, container, false)

        binding.timetext.text = date.toString()

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapimage) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.mapimage, it).commit()
            }
        mapFragment.getMapAsync(this)

        binding.transparentbutton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, ParkingMap())
                .addToBackStack(null)
                .commit()
        }

        binding.inbutton.setOnClickListener {
            recordEntryTime()
        }

        binding.outbutton.setOnClickListener {
            recordExitTime()
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

    private fun recordEntryTime() {
            val userId = auth.currentUser?.uid ?: return
            val entryTime = System.currentTimeMillis()

            val record = hashMapOf(
                "entrytime" to entryTime,
                "exittime" to null
            )

            firestore.collection("users").document(userId)
                .collection("parking_records").document("duration")
                .set(record)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "입차 시간이 기록되었습니다", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "입차 시간 기록 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun recordExitTime() {
            val userId = auth.currentUser?.uid ?: return
            val exitTime = System.currentTimeMillis()

            firestore.collection("users").document(userId)
                .collection("parking_records").document("duration")
                .get()
                .addOnSuccessListener { document ->
                    if(document != null && document.contains("entrytime")) {
                        val entryTime = document.getLong("entrytime") ?: return@addOnSuccessListener
                        val durationMill = exitTime - entryTime

                        val update = mapOf(
                            "exittime" to exitTime,
                            "duration" to durationMill
                        )

                        firestore.collection("users").document(userId)
                            .collection("parking_records").document("duration")
                            .update(update)
                            .addOnSuccessListener {
                                val durationSecs = durationMill / 1000
                                Toast.makeText(requireContext(), "총 주차시간: ${durationSecs}초", Toast.LENGTH_SHORT).show()
                                if (durationSecs / 60 < 30) {
                                    binding.parkingfee.text = "30분 무료 ${durationSecs/60}분 주차중"
                                } else if (durationSecs / 60 < 60) {
                                    binding.parkingfee.text = "2000원 ${durationSecs/60}분 주차중"
                                } else {
                                    val durationMin = ((durationSecs / 60) - 60) / 30
                                    binding.parkingfee.text = "${2000+(500 * (durationMin+1))}원 ${durationSecs/60}분 주차중"
                                }


                            }
                            .addOnFailureListener{ e ->
                                Toast.makeText(requireContext(), "입차 기록이 없습니다", Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        Toast.makeText(requireContext(), "입차 기록이 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "데이터를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                }
        }
}