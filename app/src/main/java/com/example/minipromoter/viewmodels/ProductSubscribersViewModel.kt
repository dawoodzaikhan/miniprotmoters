package com.example.minipromoter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.ProductModel

class ProductSubscribersViewModel(productModel: ProductModel) : ViewModel() {

    //user list live data to get data from db
    val userList =
        App.getUserRepository().database.productSubscribersDao.getProductSubscribers(productModel.productId)


    //factory call to create object
    class Factory(val productModel: ProductModel) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductSubscribersViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductSubscribersViewModel(productModel) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}
