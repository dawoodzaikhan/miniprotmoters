package com.example.minipromoter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.minipromoter.adapter.OnClickListener
import com.example.minipromoter.adapter.ProductSelectionAdapter
import com.example.minipromoter.databinding.FragmentProductsBinding
import com.example.minipromoter.dialogs.AddNewProductDialog
import com.example.minipromoter.viewmodels.FragmentProductViewModel

class ProductsFragment : Fragment() {

    private val viewModel: FragmentProductViewModel by lazy {
        ViewModelProvider(
            this,
            FragmentProductViewModel.Factory()
        ).get(FragmentProductViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProductsBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //assigning adapter to recycle view
        binding.rvProduct.adapter = ProductSelectionAdapter(

            //forwarding click listeners
            OnClickListener(clickListener = {

                val directions =
                    ProductsFragmentDirections.actionProductsFragmentToCampainsFragment(it)
                findNavController().navigate(directions)

            }, subscriberListener = {
                findNavController().navigate(
                    ProductsFragmentDirections.actionProductsFragmentToProductSubscribers(
                        productModel = it
                    )
                )

            })
        )

        // adding line each item in recycle view
        binding.rvProduct.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        //observing products live data so we can notify the adapter
        viewModel.product.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvProduct.adapter as ProductSelectionAdapter
                adapter.submitList(it)
            }
        })

        //floating action button click listener
        binding.fbAdd.setOnClickListener {

            // show add new product dialog
            showAddNewProductFragment()
        }

        return binding.root
    }


    private fun showAddNewProductFragment() {

        // creating new instance of dialog and showing
        AddNewProductDialog.newInstance().show(childFragmentManager, "dialog")

    }

}
