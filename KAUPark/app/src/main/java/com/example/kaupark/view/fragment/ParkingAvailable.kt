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
    // ParkingSpot은 name, currentleft, total로 이루어져있음
    // ParkingSpotList는 ParkingSpot으로 이루어져 있음
    private val parkingSpotList = arrayListOf<ParkingSpot>()

    private lateinit var binding: ParkingAvailableBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ParkingAvailableBinding.inflate(inflater, container, false)
        loadParkingData()
        return binding.root
    }
    // 남색 : 현재 주차되어있는 여석 개수
    // 흰색 : 남은 여석
    private fun loadParkingData() {
        firestore.collection("parkingAvailable")
            // parkingAvailable 컬렉션에 있는 모든 문서 가져오기
            .get()
            // documents는 parkingAvailable의 각각의 문서를 나타냄
            .addOnSuccessListener { documents ->
                parkingSpotList.clear()

                // documents안에 있는 원소들을 순회
                for (document in documents) {

                    // firebase에 있는 값 세팅
                    val name = document.id
                    val currentLeft = document.getLong("currentLeft")?.toInt() ?: 0
                    val total = document.getLong("total")?.toInt() ?: 0

                    // 위에 있는 값드을 담고 있는 객체 생성
                    val parkingSpot = ParkingSpot(name, currentLeft, total)
                    // 리스트에 추가
                    parkingSpotList.add(parkingSpot)
                }

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
            // 남색부분 : total - currentleft, 현재 주차되어있는 공간 개수
            add(PieEntry((spot.total - spot.currentLeft).toFloat(), "사용 중"))
            // 흰색부분 : 현재 남아있는 공간 개수
            add(PieEntry(spot.currentLeft.toFloat(), "남은 자리"))
        }

        val colorsItems = ArrayList<Int>().apply {
            add(Color.parseColor("#40368A"))
            add(Color.parseColor("#FFFFFF"))
        }

        val pieDataSet = PieDataSet(entries, "").apply {
            colors = colorsItems
            valueTextColor = Color.WHITE
            valueTextSize = 16f
            setDrawValues(false) // 테두리 숫자 제거
        }

        // 파이차트에 표시할 데이터 생성
        val pieData = PieData(pieDataSet)

        pieChart.apply {
            data = pieData
            setHoleColor(Color.parseColor("#D9D9D9"))
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false) // 항목 이름도 표시하지 않음
            animateY(1400, Easing.EaseInOutQuad)

            // 중앙 텍스트 설정 (한 줄로 변경)
            val centerText = "${spot.currentLeft}  / ${spot.total} "
            setCenterText(centerText) // 텍스트 설정
            setCenterTextColor(Color.BLACK) // 텍스트 색상
            setCenterTextSize(12f) // 텍스트 크기 (조정)

            invalidate() // 업데이트
        }
    }

}

