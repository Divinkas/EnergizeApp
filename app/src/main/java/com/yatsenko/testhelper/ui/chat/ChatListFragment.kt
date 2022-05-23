package com.yatsenko.testhelper.ui.chat

import android.os.Bundle
import android.view.View
import com.yatsenko.core.utils.log
import com.yatsenko.testhelper.R
import com.yatsenko.testhelper.base.BaseFragment
import com.yatsenko.testhelper.ui.auth.model.AuthState
import com.yatsenko.testhelper.ui.chat.adapter.UserChatsAdapter
import com.yatsenko.testhelper.utils.getVerticalLinearLayoutManager
import com.yatsenko.testhelper.utils.gone
import com.yatsenko.testhelper.utils.openLoginScreen
import com.yatsenko.testhelper.utils.visibility
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.observeAuthState(viewLifecycleOwner)
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
            userChatAdapter?.updateItems(chats)
            rvUserChats?.visibility(chats.isNotEmpty())
            tvEmptyChatsLabel?.visibility(chats.isEmpty())
            progress?.gone()
        }

        viewModel.authLiveData.observe(viewLifecycleOwner) { authState ->
            when (authState) {
                is AuthState.AuthSuccess -> log("[AuthSuccess]")
                is AuthState.AuthError -> activity?.openLoginScreen()
            }
        }
    }

    private fun openChat(chatId: String) {
        navigateTo(ChatListFragmentDirections.toFragmentMessenger(chatId))
    }
}