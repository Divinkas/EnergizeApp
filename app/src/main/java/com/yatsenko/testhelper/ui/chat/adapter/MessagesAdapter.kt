package com.yatsenko.testhelper.ui.chat.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yatsenko.core.bean.Message
import com.yatsenko.testhelper.R
import com.yatsenko.testhelper.databinding.ItemMyMessageBinding
import com.yatsenko.testhelper.databinding.ItemOtherMessageBinding
import com.yatsenko.testhelper.utils.DateUtils
import com.yatsenko.testhelper.utils.createView

class MessagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Message>()

    var currentUserId: String = ""

    @SuppressLint("NotifyDataSetChanged")
    fun addMessages(newMessages: List<Message>) {
        items.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MESSAGE_TYPE_DEFAULT -> {
                OtherMessageViewHolder(ItemOtherMessageBinding.bind(parent.createView(R.layout.item_other_message)))
            }
            else -> {
                MyMessageViewHolder(ItemMyMessageBinding.bind(parent.createView(R.layout.item_my_message)))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OtherMessageViewHolder) {
            holder.binding.tvOtherMessage.text = items[position].message
            if (items[position].timestamp != 0L) {
                holder.binding.tvOtherMessageTime.text = DateUtils.getFormattedDate(items[position].timestamp)
            }
        }
        if (holder is MyMessageViewHolder) {
            holder.binding.tvMyMessage.text = items[position].message
            if (items[position].timestamp != 0L) {
                holder.binding.tvMyMessageTime.text = DateUtils.getFormattedDate(items[position].timestamp)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].userId != currentUserId) MESSAGE_TYPE_DEFAULT else MESSAGE_TYPE_MY
    }

    override fun getItemCount(): Int = items.size

    class MyMessageViewHolder(val binding: ItemMyMessageBinding) : RecyclerView.ViewHolder(binding.root)

    class OtherMessageViewHolder(val binding: ItemOtherMessageBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private const val MESSAGE_TYPE_DEFAULT = 9999
        private const val MESSAGE_TYPE_MY = 9998
    }
}