package com.example.kaupark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.ListPersonBinding
import com.example.kaupark.model.Person
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale

class PersonsAdapter(private val personList: MutableList<Person>, private val carNum: String, private val onItemClick: (Person) -> Unit) : RecyclerView.Adapter<PersonsAdapter.Holder>() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) // 클래스 수준에서 정의

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListPersonBinding.inflate(LayoutInflater.from(parent.context))
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

    // 데이터 업데이트 메소드
    fun updateData(newList: List<Person>) {
        personList.clear()
        personList.addAll(newList)
        notifyDataSetChanged()
    }

    class Holder(private val binding: ListPersonBinding, private val carNum: String, private val onItemClick: (Person) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(person: Person) {
            // 참가자 이름 선택
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
