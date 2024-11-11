package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.kaupark.R


class ParkingPayment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // R.id.gender_spinner 는 1번에서 지정한 Spinner 태그의 ID 입니다.
        val spinner: Spinner = findViewById(R.id.ParkingCar_Number)

        ArrayAdapter.createFromResource(
            this,

            // R.array.gender_array 는 2번에서 설정한 string-array 태그의 name 입니다.
            R.array.ParkingCar_Number,

            // android.R.layout.simple_spinner_dropdown_item 은 android 에서 기본 제공
            // 되는 layout 입니다. 이 부분은 "선택된 item" 부분의 layout을 결정합니다.
            android.R.layout.simple_spinner_dropdown_item

        ).also { adapter ->

            // android.R.layout.simple_spinner_dropdown_item 도 android 에서 기본 제공
            // 되는 layout 입니다. 이 부분은 "선택할 item 목록" 부분의 layout을 결정합니다.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}
