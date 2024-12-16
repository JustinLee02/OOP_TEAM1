package com.example.kaupark.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.R
import com.example.kaupark.model.ChatModel
import java.text.SimpleDateFormat
import java.util.Locale

// ChatAdapter 클래스는 RecyclerView를 위한 어댑터로, 채팅 데이터를 UI에 바인딩
class ChatAdapter(private val currentUser: String, private val itemList: ArrayList<ChatModel>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    // 날짜와 시간을 포맷팅하기 위한 SimpleDateFormat 객체 (클래스 수준에서 정의)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd a hh:mm", Locale.getDefault())

    // 뷰 홀더를 생성하는 메서드, 레이아웃 XML을 inflate하여 ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_chat_layout, parent, false)
        return ViewHolder(view)
    }

    // 데이터 세트의 크기를 반환하는 메서드
    override fun getItemCount(): Int = itemList.size

    // 뷰 홀더와 데이터를 바인딩하는 메서드
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = itemList[position]

        // 현재 사용자와 채팅의 작성자가 동일한지 확인하여 카드 배경 색상 변경
        val cardColor = if (currentUser == chat.nickname) "#FFF176" else "#FFFFFF"
        holder.card.setCardBackgroundColor(Color.parseColor(cardColor)) // 카드 배경 색상 설정

        // 채팅 작성자 닉네임 및 내용 설정
        holder.nickname.text = chat.nickname
        holder.contents.text = chat.contents

        // 시간 및 날짜 설정, null인 경우 "Unknown" 표시
        holder.time.text = chat.time?.let { dateFormat.format(it) } ?: "Unknown"
    }

    // 데이터 리스트를 업데이트하고 RecyclerView를 갱신하는 메서드
    fun updateList(newList: List<ChatModel>) {
        itemList.clear() // 기존 리스트 초기화
        itemList.addAll(newList) // 새로운 리스트 추가
        notifyDataSetChanged() // 데이터 변경 알림
    }

    // RecyclerView의 ViewHolder 클래스, 개별 항목의 뷰를 관리
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 카드 뷰
        val card: CardView = itemView.findViewById(R.id.chat_card_view)
        // 닉네임 텍스트 뷰
        val nickname: TextView = itemView.findViewById(R.id.textview_nickname)
        // 채팅 내용 텍스트 뷰
        val contents: TextView = itemView.findViewById(R.id.textview_contents)
        // 채팅 시간 텍스트 뷰
        val time: TextView = itemView.findViewById(R.id.textview_messagetime)
    }
}
