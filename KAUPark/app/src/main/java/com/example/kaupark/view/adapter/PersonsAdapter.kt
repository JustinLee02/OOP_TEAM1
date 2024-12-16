package com.example.kaupark.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.databinding.RecyclerviewListPersonBinding
import com.example.kaupark.model.PersonModel
import java.text.SimpleDateFormat
import java.util.Locale

// PersonsAdapter: RecyclerView의 어댑터로, 사람 목록(personList)을 표시하고 상호작용을 처리
class PersonsAdapter(
    private val personList: MutableList<PersonModel>, // 표시할 데이터 리스트
    private val carNum: String, // 현재 사용자의 차량 번호
    private val onItemClick: (PersonModel) -> Unit // 항목 클릭 이벤트 처리
) : RecyclerView.Adapter<PersonsAdapter.Holder>() {

    // 날짜 및 시간 포맷을 정의하는 SimpleDateFormat 객체
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // ViewHolder 생성: 레이아웃을 inflate하여 Holder 인스턴스 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerviewListPersonBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding, carNum, onItemClick)
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(personList[position])
    }

    // RecyclerView 항목 수 반환
    override fun getItemCount(): Int = personList.size

    // 특정 위치의 아이템을 삭제하는 메서드
    fun removePerson(position: Int) {
        if (position in 0 until personList.size) { // 유효한 범위 확인
            personList.removeAt(position) // 리스트에서 항목 삭제
            notifyItemRemoved(position) // RecyclerView에 변경 알림
        }
    }

    // 데이터 업데이트: 기존 리스트를 새로운 리스트로 교체
    fun updateData(newList: List<PersonModel>) {
        personList.clear() // 기존 데이터 제거
        personList.addAll(newList) // 새로운 데이터 추가
        notifyDataSetChanged() // RecyclerView 갱신
    }

    // ViewHolder 클래스: 개별 항목의 뷰를 관리
    class Holder(
        private val binding: RecyclerviewListPersonBinding, // 데이터 바인딩 객체
        private val carNum: String, // 현재 사용자의 차량 번호
        private val onItemClick: (PersonModel) -> Unit // 항목 클릭 이벤트 처리
    ) : RecyclerView.ViewHolder(binding.root) {

        // 데이터를 뷰에 바인딩
        fun bind(person: PersonModel) {
            // 표시할 이름: 현재 사용자의 차량 번호와 비교하여 다른 사용자를 표시
            binding.textviewPersonname.text =
                if (person.participants[0] == carNum) person.participants[1] else person.participants[0]

            // 날짜 및 시간 포맷팅하여 표시
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            binding.textviewListtime.text = dateFormat.format(person.currentTime)

            // 마지막 메시지를 표시하거나 기본값으로 "새로운 메시지" 설정
            binding.textviewMessage.text = person.lastMessage ?: "새로운 메시지"

            // 항목 클릭 이벤트 설정
            binding.root.setOnClickListener { onItemClick(person) }
        }
    }
}
