package com.yatsenko.testhelper.ui.chat

import android.os.Bundle
import android.view.View
import com.yatsenko.testhelper.R
import com.yatsenko.testhelper.base.BaseFragment
import com.yatsenko.testhelper.ui.chat.adapter.UserChatsAdapter
import com.yatsenko.testhelper.utils.getVerticalLinearLayoutManager
import com.yatsenko.testhelper.utils.gone
import com.yatsenko.testhelper.utils.visible
import kotlinx.android.synthetic.main.fragment_chat_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatListFragment : BaseFragment() {

    override var layoutRes: Int = R.layout.fragment_chat_list

    private val viewModel: ChatViewModel by viewModel()

    private var userChatAdapter: UserChatsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupListeners()
        observeData()

        viewModel.connectAuthSocket()
        viewModel.connectToChatSocket()
    }

    override fun onResume() {
        super.onResume()
        viewModel.observeChatCreateResponse(viewLifecycleOwner)
        viewModel.observeJoinChatResponse(viewLifecycleOwner)

        viewModel.loadChatList()
    }

    private fun initView() {
        userChatAdapter = UserChatsAdapter()
        userChatAdapter?.selectChatEvent = { chatId ->
            openChat(chatId)
        }

        rvUserChats?.layoutManager = context?.getVerticalLinearLayoutManager()
        rvUserChats?.adapter = userChatAdapter
    }

    private fun setupListeners() {
        btnCreateNewChat?.setOnClickListener {
            viewModel.createChat()
        }
    }

    private fun observeData() {
        viewModel.userChatsLiveData.observe(viewLifecycleOwner) { chats ->
            if (chats.isEmpty()) {
                showEmptyChatsView()
            } else {
                userChatAdapter?.updateItems(chats)
                rvUserChats?.visible()
            }
            progress?.gone()
        }
    }

    private fun showEmptyChatsView() {
        rvUserChats?.gone()
    }

    private fun openChat(chatId: String) {
        navigateTo(ChatListFragmentDirections.toFragmentMessenger(chatId))
    }
}