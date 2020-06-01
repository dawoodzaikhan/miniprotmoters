package com.example.minipromoter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.minipromoter.adapter.CampaignAdapter
import com.example.minipromoter.adapter.CampainOnClickListener
import com.example.minipromoter.databinding.FragmentCampainsBinding
import com.example.minipromoter.dialogs.AddNewCampaignDialog
import com.example.minipromoter.viewmodels.CampaignViewModel
import kotlinx.android.synthetic.main.fragment_campains.*

/**
 * A simple [Fragment] subclass.
 */
class CampaignsFragment : Fragment() {

    private val args: CampaignsFragmentArgs by navArgs()

    private val viewModel: CampaignViewModel by lazy {
        ViewModelProvider(
            this,
            CampaignViewModel.Factory(args.productModel)
        ).get(CampaignViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCampainsBinding.inflate(inflater)

        binding.rvCampains.adapter = CampaignAdapter(CampainOnClickListener {
            findNavController().navigate(
                CampaignsFragmentDirections.actionCampainsFragmentToCampainMessagesFragment(
                    it
                )
            )
        })

        binding.rvCampains.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.fbAdd.setOnClickListener {
            AddNewCampaignDialog.newInstance(args.productModel).show(childFragmentManager, "dialog")

        }

        observeVariables()

        return binding.root
    }

    private fun observeVariables() {
        viewModel.campaignList.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = rvCampains.adapter as CampaignAdapter
                adapter.submitList(it)
            }
        })
    }

}
