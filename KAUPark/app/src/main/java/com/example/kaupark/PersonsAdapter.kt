package com.example.kaupark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.ListPersonBinding

class PersonsAdapter(val p: Array<Person>) :RecyclerView.Adapter<PersonsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListPersonBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(p[position])
    }

    override fun getItemCount() = p.size

    class Holder(private val binding : ListPersonBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(p: Person) {
            binding.txtName.text = p.carNum
            binding.txtMessage.text = p.text
            binding.txtTime.text = p.time

            binding.root.setOnClickListener(){
                // 화면 전환
            }

        }
    }

}