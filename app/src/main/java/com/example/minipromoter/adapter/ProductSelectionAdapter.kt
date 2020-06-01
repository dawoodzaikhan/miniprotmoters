package com.example.minipromoter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minipromoter.R
import com.example.minipromoter.databinding.ProductItemBinding
import com.example.minipromoter.models.ProductModel

class ProductSelectionAdapter(val onClickListener: OnClickListener) :
    ListAdapter<ProductModel, ProductSelectionAdapter.ViewHolder>(
        DiffCallBack
    ) {

    inner class ViewHolder(private var binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(client: ProductModel) {
            binding.model = client

            binding.tvSubscribers.setOnClickListener {
                onClickListener.onSubscribersClicked(client)
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ProductItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.product_item,
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

    companion object DiffCallBack : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(
            oldItem: ProductModel,
            newItem: ProductModel
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductModel,
            newItem: ProductModel
        ): Boolean {
            return oldItem.productName == newItem.productName
        }
    }
}

class OnClickListener(
    val clickListener: (client: ProductModel) -> Unit,
    val subscriberListener: (client: ProductModel) -> Unit
) {
    fun onClick(client: ProductModel) = clickListener(client)
    fun onSubscribersClicked(client: ProductModel) = subscriberListener(client)
}