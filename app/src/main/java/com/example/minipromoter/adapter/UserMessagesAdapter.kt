package com.example.minipromoter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minipromoter.R
import com.example.minipromoter.databinding.UserMessageItemBinding
import com.example.minipromoter.models.UserMessage

class UserMessagesAdapter(private val onClickListener: UserMessageOnClickListener) :
    ListAdapter<UserMessage, UserMessagesAdapter.ViewHolder>(
        DiffCallBack
    ) {

    class ViewHolder(private var binding: UserMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(client: UserMessage) {

            binding.model = client


            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<UserMessageItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.user_message_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(client)
        }
        holder.bind(client)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<UserMessage>() {
        override fun areItemsTheSame(
            oldItem: UserMessage,
            newItem: UserMessage
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: UserMessage,
            newItem: UserMessage
        ): Boolean {
            return oldItem.messageId == newItem.messageId
        }
    }
}

class UserMessageOnClickListener(val clickListener: (client: UserMessage) -> Unit) {
    fun onClick(client: UserMessage) = clickListener(client)
}
