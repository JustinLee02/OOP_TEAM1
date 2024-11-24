import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaupark.ChatAdapter
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private val chatViewModel: ChatViewModel by activityViewModels()
    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var currentUser: String
    private lateinit var receiver: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUser = arguments?.getString("currentUser").toString()
        receiver = arguments?.getString("receiver").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.recyclerviewChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        chatViewModel.loadMessages(currentUser, receiver)

        chatViewModel.chatList.observe(viewLifecycleOwner, Observer { chatList ->
            adapter = ChatAdapter(currentUser, ArrayList(chatList))
            binding.recyclerviewChat.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        binding.edittextMessage.addTextChangedListener { text ->
            binding.buttonSendmessage.isEnabled = text.toString().isNotEmpty()
        }

        binding.buttonSendmessage.setOnClickListener {
            val message = binding.edittextMessage.text.toString()
            chatViewModel.sendMessage(currentUser, receiver, message)
            binding.edittextMessage.setText("")
        }

        binding.toolbarChat.setNavigationIcon(R.drawable.arrow_back)
        binding.toolbarChat.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(currentUser: String, receiver: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString("currentUser", currentUser)
                    putString("receiver", receiver)
                }
            }
    }
}
