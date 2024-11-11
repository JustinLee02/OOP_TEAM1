package com.example.kaupark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.ListPersonBinding
import com.example.kaupark.model.Person
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PersonsAdapter(private val personList: MutableList<Person>) : RecyclerView.Adapter<PersonsAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListPersonBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
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

    class Holder(private val binding: ListPersonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(person: Person) {
            binding.txtName.text = person.carNum
            binding.txtTime.text = person.currentTime
        }
    }
}