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

    // ViewModel을 Fragment에서 공유하여 사용하기 위해 activityViewModels() 사용
    private val chatViewModel: ChatViewModel by activityViewModels()

    // ViewBinding을 위한 변수
    private lateinit var binding: FragmentChatBinding

    // RecyclerView의 어댑터를 관리하는 변수
    private var adapter: ChatAdapter? = null

    // 현재 사용자와 상대방의 식별자
    private lateinit var currentUser: String
    private lateinit var receiver: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // arguments에서 현재 사용자와 수신자 정보를 가져옴
        currentUser = arguments?.getString("currentUser").toString()
        receiver = arguments?.getString("receiver").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding을 통해 레이아웃 초기화
        binding = FragmentChatBinding.inflate(inflater, container, false)

        // RecyclerView 레이아웃 매니저 설정
        binding.recyclerviewChat.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )

        // 메시지 로딩을 코루틴으로 실행
        lifecycleScope.launch {
            chatViewModel.loadMessages(currentUser, receiver)
        }

        // LiveData 옵저버 설정
        setUpObservers()

        // 입력 필드의 텍스트 변경에 따라 전송 버튼 활성화 여부 결정
        binding.edittextMessage.addTextChangedListener { text ->
            binding.buttonSendmessage.isEnabled = text.toString().isNotEmpty()
        }

        // 전송 버튼 클릭 시 메시지 전송 로직 실행
        binding.buttonSendmessage.setOnClickListener {
            val message = binding.edittextMessage.text.toString()
            lifecycleScope.launch {
                chatViewModel.sendMessage(currentUser, receiver, message)
            }

            // 메시지 전송 후 입력 필드를 초기화
            binding.edittextMessage.setText("")
        }

        // 툴바에 상대방 이름 설정 및 색상 적용
        binding.toolbarChat.title = receiver
        binding.toolbarChat.setTitleTextColor(
            ContextCompat.getColor(requireContext(), android.R.color.white)
        )

        // 뒤로 가기 아이콘 및 클릭 이벤트 설정
        binding.toolbarChat.setNavigationIcon(R.drawable.icon_arrow_back)
        binding.toolbarChat.setNavigationOnClickListener {
            parentFragmentManager.popBackStack() // 이전 화면으로 이동
        }

        return binding.root // 뷰 반환
    }

    private fun setUpObservers() {
        // 채팅 목록 업데이트를 관찰
        chatViewModel.chatList.observe(viewLifecycleOwner, Observer { chatList ->
            if (adapter == null) {
                // 어댑터가 없으면 초기화 후 RecyclerView에 연결
                adapter = ChatAdapter(currentUser, ArrayList(chatList))
                binding.recyclerviewChat.adapter = adapter
            } else {
                // 기존 어댑터에 데이터 업데이트
                adapter?.updateList(chatList)
            }

            // 새로운 메시지가 있으면 가장 마지막 메시지로 스크롤
            if (chatList.isNotEmpty()) {
                binding.recyclerviewChat.scrollToPosition(chatList.size - 1)
            }
        })
    }

    companion object {
        // ChatFragment의 인스턴스를 생성하는 팩토리 메서드
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
