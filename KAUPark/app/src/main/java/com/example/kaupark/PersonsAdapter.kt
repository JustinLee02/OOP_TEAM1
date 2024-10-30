package com.example.kaupark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.ListPersonBinding

class PersonsAdapter(private val personList: MutableList<Person>) : RecyclerView.Adapter<PersonsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListPersonBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(personList[position])
    }

    override fun getItemCount() = personList.size

    fun addPerson(person: Person) {
        personList.add(0, person)
        notifyItemInserted(0)
    }



    class Holder(private val binding: ListPersonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(person: Person) {
            binding.txtName.text = person.carNum
            binding.txtMessage.text = person.text
            binding.txtTime.text = person.time
        }
    }
}