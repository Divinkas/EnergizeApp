package com.yatsenko.testhelper.ui.chat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yatsenko.core.bean.Message
import com.yatsenko.testhelper.databinding.ItemMyMessageBinding
import com.yatsenko.testhelper.databinding.ItemOtherMessageBinding

class MessagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Message>()

    var currentUserId: String = ""

    fun addMessages(newMessages: List<Message>) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = items.size

    class MyMessageViewHolder(val binding: ItemMyMessageBinding): RecyclerView.ViewHolder(binding.root)

    class OtherMessageViewHolder(val binding: ItemOtherMessageBinding): RecyclerView.ViewHolder(binding.root)
}