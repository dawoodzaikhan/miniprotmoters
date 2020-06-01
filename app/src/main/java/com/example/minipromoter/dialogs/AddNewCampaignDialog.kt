package com.example.minipromoter.dialogs

import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.R
import com.example.minipromoter.Utils.EventObserver
import com.example.minipromoter.databinding.FragmentAddNewCampainDialogListDialogBinding
import com.example.minipromoter.models.ProductModel
import com.example.minipromoter.viewmodels.AddNewCampaignDialogViewModel
import kotlinx.android.synthetic.main.fragment_add_new_campain_dialog_list_dialog.*


private const val PRODUCT_NAME = "param1"

class AddNewCampaignDialog : BottomSheetDialogFragment() {

    private var productName: ProductModel? = null

    //view model for dialog
    private val viewModel: AddNewCampaignDialogViewModel by lazy {
        ViewModelProvider(
            this,
            AddNewCampaignDialogViewModel.Factory(productName!!)
        ).get(AddNewCampaignDialogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productName = it.getParcelable(PRODUCT_NAME)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddNewCampainDialogListDialogBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        observerVariables()

        return binding.root


    }

    private fun observerVariables() {

        viewModel.onButtonClicked.observe(viewLifecycleOwner, EventObserver {
            when {
                viewModel.tittle.value!!.isEmpty() -> ilName.error =
                    getString(R.string.enter_campain_name)
                viewModel.message.value!!.isEmpty() -> tiMessage.error =
                    getString(R.string.enter_campain_message)
                else -> {
                    viewModel.addNewCampaign()
                    dismiss()
                }
            }


        })
    }


    companion object {
        fun newInstance(product: ProductModel): AddNewCampaignDialog =
            AddNewCampaignDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT_NAME, product as Parcelable)

                }
            }

    }
}
