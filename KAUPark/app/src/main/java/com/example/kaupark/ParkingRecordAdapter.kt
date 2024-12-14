package com.example.kaupark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kaupark.databinding.ParkingrecordRecyclerLayoutBinding
import com.example.kaupark.model.ParkingItem

class ParkingRecordAdapter(private val parkingItems: List<ParkingItem>): RecyclerView.Adapter<ParkingRecordAdapter.ParkingViewHolder>() {
    inner class ParkingViewHolder(private val binding: ParkingrecordRecyclerLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ParkingItem) {
            binding.textviewTime.text = item.date
            binding.textviewWon.text = item.fee
            binding.textviewClock.text = item.duration

            Glide.with(binding.imageviewCarGif.context)
                .asGif()
                .load(R.drawable.cargif)
                .into(binding.imageviewCarGif)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingViewHolder {
        val binding = ParkingrecordRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParkingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParkingViewHolder, position: Int) {
        holder.bind(parkingItems[position])

    }

    override fun getItemCount(): Int = parkingItems.size

}