package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.ProductModel

//
// Created by Abdul Basit on 3/12/2020.
//

class CampaignViewModel(productModel: ProductModel) : BaseViewModel() {

    //campaign list live data
    val campaignList =
        App.getUserRepository().database.campaignDao.getProductsCampaigns(productModel.productId)

    //toolbar tittle live data
    val toolbarTittle = MutableLiveData("Campaigns")


    //factory to create view model
    class Factory(private val productName: ProductModel) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CampaignViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CampaignViewModel(productName) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

}