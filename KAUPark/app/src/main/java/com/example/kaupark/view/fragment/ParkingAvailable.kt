package com.example.kaupark.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kaupark.databinding.ParkingAvailableBinding
import com.example.kaupark.model.ParkingSpot
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.firestore.FirebaseFirestore

class ParkingAvailable : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private val parkingSpotList = arrayListOf<ParkingSpot>()

    private lateinit var binding: ParkingAvailableBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 설정
        binding = ParkingAvailableBinding.inflate(inflater, container, false)

        // Firestore에서 데이터 가져오기
        loadParkingData()

        return binding.root
    }

    private fun loadParkingData() {
        firestore.collection("parkingAvailable")
            .get()
            .addOnSuccessListener { documents ->
                parkingSpotList.clear() // 기존 리스트 초기화

                for (document in documents) {
                    // Firestore 문서에서 데이터 추출
                    val name = document.id // 건물 이름
                    val currentLeft = document.getLong("currentLeft")?.toInt() ?: 0
                    val total = document.getLong("total")?.toInt() ?: 0

                    // ParkingSpot 객체 생성 후 리스트에 추가
                    val parkingSpot = ParkingSpot(name, currentLeft, total)
                    parkingSpotList.add(parkingSpot)
                }

                // PieChart에 데이터 표시
                displayParkingData()
            }
            .addOnFailureListener { exception ->
                Log.e("ParkingAvailable", "Error getting documents: ", exception)
            }
    }

    private fun displayParkingData() {
        // 각 주차장 데이터를 PieChart로 표현
        for (spot in parkingSpotList) {
            when (spot.name) {
                "library" -> updatePieChart(binding.libraryPiechart, spot)
                "studentCenter" -> updatePieChart(binding.studentCenterPiechart, spot)
                "academicBuilding" -> updatePieChart(binding.academicBuildingPiechart, spot)
                "scienceBuilding" -> updatePieChart(binding.scienceBuildingPiechart, spot)
                "searchBuilding" -> updatePieChart(binding.searchBuildingPiechart, spot)
                "somethingBuilding" -> updatePieChart(binding.somethingBuildingPiechart, spot)
            }
        }
    }

    private fun updatePieChart(pieChart: com.github.mikephil.charting.charts.PieChart, spot: ParkingSpot) {
        // PieChart 데이터 구성
        val entries = ArrayList<PieEntry>().apply {
            // 노란색: 사용 중, 하얀색: 남은 자리
            add(PieEntry((spot.total - spot.currentLeft).toFloat(), "사용 중")) // 사용 중
            add(PieEntry(spot.currentLeft.toFloat(), "남은 자리")) // 남은 자리
        }

        val colorsItems = ArrayList<Int>().apply {
            add(Color.parseColor("#40368A")) // 사용 중 (하얀색)
            add(Color.parseColor("#FFFFFF")) // 남은 자리 (파란색)
        }

        val pieDataSet = PieDataSet(entries, "").apply {
            colors = colorsItems
            valueTextColor = Color.WHITE // 기본 텍스트 색상 설정
            valueTextSize = 16f
        }

        val pieData = PieData(pieDataSet)

        // 값 포맷터 설정 (정수형으로 표시)
        pieData.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString() // 정수로 변환
            }
        })

        // PieChart 설정
        pieChart.apply {
            data = pieData
            setHoleColor(Color.parseColor("#D9D9D9")) // 가운데 원형 색상
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false)
            animateY(1400, Easing.EaseInOutQuad)
            invalidate() // 업데이트
        }
    }
}
