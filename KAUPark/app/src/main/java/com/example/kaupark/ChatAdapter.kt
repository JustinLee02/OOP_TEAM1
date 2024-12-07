import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kaupark.R
import com.example.kaupark.model.Chat
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter(private val currentUser: String, private val itemList: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = itemList[position]

        if (currentUser == chat.nickname) {
            holder.card.setCardBackgroundColor(Color.parseColor("#FFF176"))
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE)
        }

        holder.nickname.text = chat.nickname
        holder.contents.text = chat.contents

        // 시간 및 날짜 포맷
        val dateFormat = SimpleDateFormat("yyyy-MM-dd a hh:mm", Locale.getDefault())
        holder.time.text = chat.time?.let { dateFormat.format(it) } ?: "Unknown"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.chat_card_view)
        val nickname: TextView = itemView.findViewById(R.id.textview_nickname)
        val contents: TextView = itemView.findViewById(R.id.textview_contents)
        val time: TextView = itemView.findViewById(R.id.textview_messagetime)
    }
}
