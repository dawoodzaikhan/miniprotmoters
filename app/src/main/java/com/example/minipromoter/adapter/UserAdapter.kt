package com.example.minipromoter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minipromoter.R
import com.example.minipromoter.databinding.ProductItemBinding
import com.example.minipromoter.databinding.UserItemBinding
import com.example.minipromoter.models.ProductModel
import com.example.minipromoter.models.UserModel

class UserAdapter(private val onClickListener: UserOnClickListener) :
    ListAdapter<UserModel, UserAdapter.ViewHolder>(
        DiffCallBack
    ) {

    class ViewHolder(private var binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(client: UserModel) {
            binding.model = client
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<UserItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.user_item,
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

    companion object DiffCallBack : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(
            oldItem: UserModel,
            newItem: UserModel
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: UserModel,
            newItem: UserModel
        ): Boolean {
            return oldItem.userId == newItem.userId
        }
    }
}

class UserOnClickListener(val clickListener: (client: UserModel) -> Unit) {
    fun onClick(client: UserModel) = clickListener(client)
}