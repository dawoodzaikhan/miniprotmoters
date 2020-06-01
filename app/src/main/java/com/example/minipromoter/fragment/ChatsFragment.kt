package com.example.minipromoter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.adapter.UserMessageOnClickListener
import com.example.minipromoter.adapter.UserMessagesAdapter
import com.example.minipromoter.databinding.FragmentChatsBinding
import com.example.minipromoter.viewmodels.ChatsViewModel

class ChatsFragment : Fragment() {
    private val args: ChatsFragmentArgs by navArgs()

    private val viewModel: ChatsViewModel by lazy {
        ViewModelProvider(
            this,
            ChatsViewModel.Factory(args.userModel)
        ).get(ChatsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChatsBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //assigning adapter
        binding.rvMessages.adapter = UserMessagesAdapter(UserMessageOnClickListener { })

        //observing the user message so we can notify
        viewModel.userMessages.observe(viewLifecycleOwner, Observer {
            val adapter=binding.rvMessages.adapter as UserMessagesAdapter
            adapter.submitList(it)
        })

        return binding.root
    }

}
