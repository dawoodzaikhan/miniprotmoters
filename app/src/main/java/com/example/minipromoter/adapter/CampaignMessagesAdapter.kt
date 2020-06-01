package com.example.minipromoter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minipromoter.R
import com.example.minipromoter.databinding.CampaignMessageItemBinding
import com.example.minipromoter.models.CampaignMessages


class CampaignMessagesAdapter(private val onClickListener: CampaignMessageOnClickListener) :
    ListAdapter<CampaignMessages, CampaignMessagesAdapter.ViewHolder>(
        DiffCallBack
    ) {

    class ViewHolder(private var binding: CampaignMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(client: CampaignMessages) {

            binding.model = client

            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<CampaignMessageItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.campaign_message_item,
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

    companion object DiffCallBack : DiffUtil.ItemCallback<CampaignMessages>() {
        override fun areItemsTheSame(
            oldItem: CampaignMessages,
            newItem: CampaignMessages
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: CampaignMessages,
            newItem: CampaignMessages
        ): Boolean {
            return oldItem.campaignId == newItem.campaignId
        }
    }
}

class CampaignMessageOnClickListener(val clickListener: (client: CampaignMessages) -> Unit) {
    fun onClick(client: CampaignMessages) = clickListener(client)
}
