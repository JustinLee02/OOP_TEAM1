package com.example.kaupark.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kaupark.databinding.ParkingAvailableBinding
import com.example.kaupark.model.ParkingSpotModel
import com.example.kaupark.viewmodel.ParkingAvailableViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import androidx.lifecycle.Observer

class ParkingAvailableFragment : Fragment() {

    private lateinit var binding: ParkingAvailableBinding
    private lateinit var viewModel: ParkingAvailableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ParkingAvailableBinding.inflate(inflater, container, false)

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(ParkingAvailableViewModel::class.java)

        // 데이터 로딩
        viewModel.loadParkingData()

        // 데이터 변화 관찰
        viewModel.parkingSpotList.observe(viewLifecycleOwner, Observer { parkingSpotList ->
            displayParkingData(parkingSpotList)
        })

        // 에러 메시지 관찰
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            Log.e("ParkingAvailable", error)
        })

        return binding.root
    }

    private fun displayParkingData(parkingSpotList: List<ParkingSpotModel>) {
        for (spot in parkingSpotList) {
            when (spot.name) {
                "library" -> {
                    updatePieChart(binding.libraryPiechart, spot)
                    setupQueueButton(binding.imageButton3, spot)
                }
                "studentCenter" -> {
                    updatePieChart(binding.studentCenterPiechart, spot)
                    setupQueueButton(binding.imageButton2, spot)
                }
                "academicBuilding" -> {
                    updatePieChart(binding.academicBuildingPiechart, spot)
                    setupQueueButton(binding.imageButton4, spot)
                }
                "scienceBuilding" -> {
                    updatePieChart(binding.scienceBuildingPiechart, spot)
                    setupQueueButton(binding.imageButton5, spot)
                }
                "searchBuilding" -> {
                    updatePieChart(binding.searchBuildingPiechart, spot)
                    setupQueueButton(binding.imageButton6, spot)
                }
                "somethingBuilding" -> {
                    updatePieChart(binding.somethingBuildingPiechart, spot)
                    setupQueueButton(binding.imageButton7, spot)
                }
            }

            // 주차 자리 현황에 따른 버튼 visibility 조정
            when (spot.name) {
                "library" -> binding.imageButton3.visibility = if (spot.currentLeft == 0) View.VISIBLE else View.GONE
                "studentCenter" -> binding.imageButton2.visibility = if (spot.currentLeft == 0) View.VISIBLE else View.GONE
                "academicBuilding" -> binding.imageButton4.visibility = if (spot.currentLeft == 0) View.VISIBLE else View.GONE
                "scienceBuilding" -> binding.imageButton5.visibility = if (spot.currentLeft == 0) View.VISIBLE else View.GONE
                "searchBuilding" -> binding.imageButton6.visibility = if (spot.currentLeft == 0) View.VISIBLE else View.GONE
                "somethingBuilding" -> binding.imageButton7.visibility = if (spot.currentLeft == 0) View.VISIBLE else View.GONE
            }

        }
    }

    private fun setupQueueButton(button: View, spot: ParkingSpotModel) {
        button.setOnClickListener {
            showQueueDialog(spot.name)
        }
    }

    private fun showQueueDialog(parkingName: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("대기열 등록")

        // 건물 이름에 맞는 메시지 생성
        val message = when(parkingName) {
            "library" -> "도서관 주차장의 대기열에 등록하시겠습니까?"
            "studentCenter" -> "학생회관 주차장의 대기열에 등록하시겠습니까?"
            "academicBuilding" -> "산학협력관 주차장의 대기열에 등록하시겠습니까?"
            "scienceBuilding" -> "과학관 주차장의 대기열에 등록하시겠습니까?"
            "searchBuilding" -> "연구동 주차장의 대기열에 등록하시겠습니까?"
            "somethingBuilding" -> "운동장 옆 주차장의 대기열에 등록하시겠습니까?"
            else -> "$parkingName 주차장의 대기열에 등록하시겠습니까?"
        }

        builder.setMessage(message)

        builder.setPositiveButton("확인") { dialog, _ ->
            // 대기열 등록 처리 로직 추가
            dialog.dismiss()
        }

        builder.setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }

        builder.create().show()
    }

    private fun updatePieChart(pieChart: com.github.mikephil.charting.charts.PieChart, spot: ParkingSpotModel) {
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
