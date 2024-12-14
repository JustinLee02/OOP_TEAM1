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
import com.google.firebase.firestore.FirebaseFirestore

class ParkingAvailable : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
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

    private fun loadParkingData() {
        firestore.collection("parkingAvailable")
            .get()
            .addOnSuccessListener { documents ->
                parkingSpotList.clear()

                for (document in documents) {
                    val name = document.id
                    val currentLeft = document.getLong("currentLeft")?.toInt() ?: 0
                    val total = document.getLong("total")?.toInt() ?: 0

                    val parkingSpot = ParkingSpot(name, currentLeft, total)
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

            // currentLeft가 0이면 이미지 버튼을 보이게 한다.
            if (spot.currentLeft == 0) {
                when (spot.name) {
                    "library" -> binding.imageButton3.visibility = View.VISIBLE
                    "studentCenter" -> binding.imageButton2.visibility = View.VISIBLE
                    "academicBuilding" -> binding.imageButton4.visibility = View.VISIBLE
                    "scienceBuilding" -> binding.imageButton5.visibility = View.VISIBLE
                    "searchBuilding" -> binding.imageButton6.visibility = View.VISIBLE
                    "somethingBuilding" -> binding.imageButton7.visibility = View.VISIBLE
                }
            } else {
                // currentLeft가 0이 아니면 이미지 버튼을 숨긴다.
                when (spot.name) {
                    "library" -> binding.imageButton3.visibility = View.GONE
                    "studentCenter" -> binding.imageButton2.visibility = View.GONE
                    "academicBuilding" -> binding.imageButton4.visibility = View.GONE
                    "scienceBuilding" -> binding.imageButton5.visibility = View.GONE
                    "searchBuilding" -> binding.imageButton6.visibility = View.GONE
                    "somethingBuilding" -> binding.imageButton7.visibility = View.GONE
                }
            }
        }
    }

    private fun updatePieChart(pieChart: com.github.mikephil.charting.charts.PieChart, spot: ParkingSpot) {
        val entries = ArrayList<PieEntry>().apply {
            add(PieEntry((spot.total - spot.currentLeft).toFloat(), "사용 중"))
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
            setDrawValues(false)
        }

        val pieData = PieData(pieDataSet)

        pieChart.apply {
            data = pieData
            setHoleColor(Color.parseColor("#D9D9D9"))
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false)
            animateY(1400, Easing.EaseInOutQuad)

            setHoleRadius(70f)
            setTransparentCircleRadius(75f)

            val centerText = "${spot.currentLeft}  / ${spot.total} "
            setCenterText(centerText)
            setCenterTextColor(Color.BLACK)
            setCenterTextSize(15f)

            invalidate()
        }
    }
}
