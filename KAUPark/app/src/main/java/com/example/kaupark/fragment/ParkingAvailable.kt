package com.example.kaupark.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kaupark.databinding.ParkingAvailableBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.animation.Easing

class ParkingAvailable : Fragment() {

    private lateinit var binding: ParkingAvailableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ParkingAvailableBinding.inflate(inflater, container, false)

        //binding.libraryPiechart.setUsePercentValues(true)

        // 가운데 원형 색상 변경
        binding.libraryPiechart.setHoleColor(Color.parseColor("#D9D9D9"))
        binding.studentCenterPiechart.setHoleColor(Color.parseColor("#D9D9D9"))
        binding.academicBuildingPiechart.setHoleColor(Color.parseColor("#D9D9D9"))
        binding.scienceBuildingPiechart.setHoleColor(Color.parseColor("#D9D9D9"))
        binding.searchBuildingPiechart.setHoleColor(Color.parseColor("#D9D9D9"))
        binding.somethingBuildingPiechart.setHoleColor(Color.parseColor("#D9D9D9"))

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(700f))
        entries.add(PieEntry(500f))

        val colorsItems = ArrayList<Int>()
        colorsItems.add(Color.parseColor("#FFFFFF"))
        colorsItems.add(Color.parseColor("#F5D509"))

        val pieDataSet = PieDataSet(entries, "").apply {
            colors = colorsItems
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }

        val pieData = PieData(pieDataSet)
        binding.libraryPiechart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

        val pieData1 = PieData(pieDataSet)
        binding.studentCenterPiechart.apply {
            data = pieData1
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }
        val pieData2 = PieData(pieDataSet)
        binding.academicBuildingPiechart.apply {
            data = pieData2
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

        val pieData3 = PieData(pieDataSet)
        binding.scienceBuildingPiechart.apply {
            data = pieData3
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

        val pieData4 = PieData(pieDataSet)
        binding.searchBuildingPiechart.apply {
            data = pieData4
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

        val pieData5 = PieData(pieDataSet)
        binding.somethingBuildingPiechart.apply {
            data = pieData5
            description.isEnabled = false
            isRotationEnabled = false
            legend.isEnabled = false
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

        return binding.root
    }
}
