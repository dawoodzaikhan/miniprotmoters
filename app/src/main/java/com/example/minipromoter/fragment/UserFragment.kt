package com.example.minipromoter.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.adapter.UserAdapter
import com.example.minipromoter.adapter.UserOnClickListener
import com.example.minipromoter.databinding.FragmentUserBinding
import com.example.minipromoter.viewmodels.UserViewModel

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {

    private val args: UserFragmentArgs by navArgs()


    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.Factory(args.prodName)).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentUserBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = UserAdapter(UserOnClickListener {

        })

        binding.rvUsers.adapter = adapter

       /* viewModel.userList.observe(viewLifecycleOwner, Observer {
            Log.d("UserFragment", "List size : " + it.size)
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        })*/

        return binding.root
    }

}
