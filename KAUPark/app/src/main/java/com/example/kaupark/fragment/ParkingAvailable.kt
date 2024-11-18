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
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.animation.Easing

class ParkingAvailable : Fragment() {

    private lateinit var binding: ParkingAvailableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
         */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ParkingAvailableBinding.inflate(inflater, container, false)

        // PieChart Setup
        binding.libraryPiechart.setUsePercentValues(true)

        // Data Set
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(508f, "Apple"))
        entries.add(PieEntry(600f, "Orange"))

        // Add a lot of colors
        val colorsItems = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
        colorsItems.add(ColorTemplate.getHoloBlue())

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
            centerText = "This is Center"
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

        return binding.root
    }

    /*
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParkingAvailable.
         */

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParkingAvailable().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    */
}
