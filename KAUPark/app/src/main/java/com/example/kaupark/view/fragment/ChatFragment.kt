import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kaupark.R
import com.example.kaupark.databinding.FragmentChatBinding
import com.example.kaupark.view.adapter.ChatAdapter
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private val chatViewModel: ChatViewModel by activityViewModels()
    private lateinit var binding: FragmentChatBinding
    private var adapter: ChatAdapter? = null
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
        setUpObservers()

        binding.edittextMessage.addTextChangedListener { text ->
            binding.buttonSendmessage.isEnabled = text.toString().isNotEmpty()
        }

        binding.buttonSendmessage.setOnClickListener {
            val message = binding.edittextMessage.text.toString()
            chatViewModel.sendMessage(currentUser, receiver, message)
            binding.edittextMessage.setText("")
        }

        binding.toolbarChat.title = receiver
        binding.toolbarChat.setTitleTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

        binding.toolbarChat.setNavigationIcon(R.drawable.icon_arrow_back)
        binding.toolbarChat.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun setUpObservers() {
        chatViewModel.chatList.observe(viewLifecycleOwner, Observer { chatList ->
            if (adapter == null) {
                adapter = ChatAdapter(currentUser, ArrayList(chatList))
                binding.recyclerviewChat.adapter = adapter
            } else {
                adapter?.updateList(chatList)
            }

            if (chatList.isNotEmpty()) {
                binding.recyclerviewChat.scrollToPosition(chatList.size - 1)
            }
        })
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
