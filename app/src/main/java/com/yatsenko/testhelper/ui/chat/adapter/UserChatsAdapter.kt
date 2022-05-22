package com.yatsenko.testhelper.ui.chat.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yatsenko.testhelper.R
import com.yatsenko.testhelper.databinding.ItemChatBinding
import com.yatsenko.testhelper.utils.createView

class UserChatsAdapter : RecyclerView.Adapter<UserChatsAdapter.ChatViewHolder>() {

    private val items = mutableListOf<String>()

    var selectChatEvent: (String) -> Unit = {}

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newChats: List<String>) {
        items.clear()
        items.addAll(newChats)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.binding.chatItemContainer.setOnClickListener {
            selectChatEvent.invoke(items[position])
        }

        holder.binding.tvChatTitle.text = items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(ItemChatBinding.bind(parent.createView(R.layout.item_chat)))
    }

    override fun getItemCount(): Int = items.size

    class ChatViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)
}