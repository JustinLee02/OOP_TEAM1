package com.example.kaupark.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.kaupark.R

object ToastHelper {
    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.toast_custom, null) // 커스텀 레이아웃 사용

        val textView: TextView = layout.findViewById(R.id.textview_toast)
        textView.text = message // 메시지 설정

        val toast = Toast(context)
        toast.duration = duration
        toast.view = layout
        toast.show()
    }
}
