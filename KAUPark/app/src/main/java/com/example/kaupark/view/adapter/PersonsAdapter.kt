package com.example.kaupark.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.RecyclerviewListPersonBinding
import com.example.kaupark.model.PersonModel
import java.text.SimpleDateFormat
import java.util.Locale


class PersonsAdapter(private val personList: MutableList<PersonModel>, private val carNum:String, private val onItemClick: (PersonModel) -> Unit) : RecyclerView.Adapter<PersonsAdapter.Holder>() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) // 클래스 수준에서 정의

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerviewListPersonBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding, carNum, onItemClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(personList[position])
    }

    override fun getItemCount(): Int = personList.size

    // 아이템 삭제 메소드 리팩토링
    fun removePerson(position: Int) {
        if (position in 0 until personList.size) {
            personList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateData(newList: List<PersonModel>) {
    // 데이터 업데이트 메소드
        personList.clear()
        personList.addAll(newList)
        notifyDataSetChanged()
    }

    class Holder(private val binding: RecyclerviewListPersonBinding,private val carNum:String, private val onItemClick: (PersonModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(person: PersonModel) {
            binding.textviewPersonname.text = if (person.participants[0] == carNum) person.participants[1] else person.participants[0]

            // 날짜 포맷
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            binding.textviewListtime.text = dateFormat.format(person.currentTime)

            // 마지막 메시지
            binding.textviewMessage.text = person.lastMessage ?: "새로운 메시지"

            // 클릭 리스너 설정
            binding.root.setOnClickListener { onItemClick(person) }
        }
    }
}
