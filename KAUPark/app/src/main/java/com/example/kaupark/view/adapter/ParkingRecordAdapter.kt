package com.example.kaupark.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kaupark.R
import com.example.kaupark.databinding.RecyclerviewParkingrecordBinding
import com.example.kaupark.model.ParkingItemModel

/**
 * 주차 기록을 표시하기 위한 RecyclerView 어댑터 클래스
 * @param parkingItems 주차 기록 데이터를 담고 있는 리스트
 */
class ParkingRecordAdapter(var parkingItems: List<ParkingItemModel>): RecyclerView.Adapter<ParkingRecordAdapter.ParkingViewHolder>() {
    /**
     * RecyclerView의 각 항목을 관리하는 ViewHolder 클래스
     * 항목 하나하나의 UI를 관리함
     * @param binding 아이템 뷰와 연결된 ViewBinding 객체
     */
    inner class ParkingViewHolder(private val binding: RecyclerviewParkingrecordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ParkingItemModel) {
            binding.textviewTime.text = item.date // 날짜
            binding.textviewWon.text = item.fee // 요금
            binding.textviewClock.text = item.duration // 주차 시간

            // Glide를 사용해 GIF 이미지를 ImageView에 로드
            Glide.with(binding.imageviewCarGif.context)
                .asGif()
                .load(R.drawable.gif_car)
                .into(binding.imageviewCarGif)
        }
    }

    /**
     * 새로운 ViewHolder 객체를 생성 (새로운 항목을 추가할 때 호출됨)
     * @param parent RecyclerView의 부모 ViewGroup
     * @param viewType 뷰 타입 (현재는 동일한 뷰 타입만 사용)
     * @return 생성된 ViewHolder 객체
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingViewHolder {
        val binding = RecyclerviewParkingrecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParkingViewHolder(binding)
    }

    /**
     * ViewHolder에 데이터를 바인딩
     * @param holder ViewHolder 객체
     * @param position 리스트 내 아이템의 위치
     */
    override fun onBindViewHolder(holder: ParkingViewHolder, position: Int) {
        holder.bind(parkingItems[position])

    }

    /**
     * RecyclerView 아이템 개수를 반환
     * @return 아이템 개수
     */
    override fun getItemCount(): Int = parkingItems.size

}