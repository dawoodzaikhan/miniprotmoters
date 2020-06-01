package com.example.minipromoter.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.viewmodels.ProductSubscribersViewModel
import com.example.minipromoter.R
import com.example.minipromoter.adapter.UserAdapter
import com.example.minipromoter.adapter.UserOnClickListener
import com.example.minipromoter.databinding.ProductSubscribersFragmentBinding


class ProductSubscribers : Fragment() {

    // arguments from previous fragment
    private val args: ProductSubscribersArgs by navArgs()

    // view models for fragment
    private val viewModel: ProductSubscribersViewModel by lazy {
        ViewModelProvider(
            this,
            ProductSubscribersViewModel.Factory(args.productModel)
        ).get(ProductSubscribersViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ProductSubscribersFragmentBinding.inflate(inflater)

        // assigning the view model and lifecycle
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //adapter object
        val adapter = UserAdapter(UserOnClickListener {

            //opening the chats fragment/screen
            findNavController().navigate(
                ProductSubscribersDirections.actionProductSubscribersToChatsFragment(
                    it
                )
            )
        })

        //assigning adapter to recyclerview
        binding.rvUsers.adapter = adapter

        //observing the live data of users
        viewModel.userList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}
