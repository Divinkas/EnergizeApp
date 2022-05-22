package com.yatsenko.testhelper.ui.chat

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.yatsenko.testhelper.R
import com.yatsenko.testhelper.base.BaseFragment
import com.yatsenko.testhelper.ui.chat.adapter.MessagesAdapter
import com.yatsenko.testhelper.utils.getVerticalLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_messenger.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessengerFragment : BaseFragment() {

    override var layoutRes: Int = R.layout.fragment_messenger

    private val viewModel: ChatViewModel by viewModel()

    private val args: MessengerFragmentArgs by navArgs()

    private var messagesAdapter: MessagesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupListeners()
        observeData()

        viewModel.connectToChatSocket()
    }

    override fun onResume() {
        super.onResume()
        viewModel.observeGetChatMessagesResponse(viewLifecycleOwner)
        viewModel.observeSendMessageResponse(viewLifecycleOwner)

        viewModel.loadMessagesByChatId(args.chatId)
    }

    private fun initView() {
        chatsMessagesTitle?.text = args.chatId

        messagesAdapter = MessagesAdapter()
        messagesAdapter?.currentUserId = viewModel.getCurrentUserId()

        rvMessages?.layoutManager = context?.getVerticalLinearLayoutManager()
        rvMessages?.adapter = messagesAdapter

        setupNavigateUpView(ivBack)
    }

    private fun setupListeners() {
        btnSendMessage?.setOnClickListener {
            if (getMessage().isNotEmpty())
                viewModel.sendMessage(getMessage(), args.chatId)
        }
    }

    private fun observeData() {
        viewModel.chatMessagesLiveData.observe(viewLifecycleOwner) { messages ->
            messagesAdapter?.addMessages(messages)
        }
    }

    private fun getMessage(): String = etMessage?.text?.toString() ?: ""
}