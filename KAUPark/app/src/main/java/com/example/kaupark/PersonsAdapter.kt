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

class PersonsAdapter(private val personList: MutableList<Person>,private val carNum:String, private val onItemClick: (Person) -> Unit) : RecyclerView.Adapter<PersonsAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListPersonBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding,carNum, onItemClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(personList[position])
    }

    override fun getItemCount() = personList.size

    fun removePerson(position: Int) {
        if (position >= 0 && position < personList.size) {
            personList.removeAt(position) // 리스트에서 아이템 삭제
            notifyItemRemoved(position) // RecyclerView에 변경 알림
        }
    }

    fun updateData(newList: List<Person>) {
        personList.clear()
        personList.addAll(newList)
        notifyDataSetChanged()
    }

    class Holder(private val binding: ListPersonBinding,private val carNum:String, private val onItemClick: (Person) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(person: Person) {
            binding.textviewPersonname.text = if (person.participants[0] == carNum) person.participants[1] else person.participants[0]
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) // 원하는 날짜 형식으로 변경 가능
            binding.textviewListtime.text = dateFormat.format(person.currentTime)

            binding.textviewMessage.text = person.lastMessage ?: "null"

            binding.root.setOnClickListener {
                onItemClick(person)
            }
        }
    }
}