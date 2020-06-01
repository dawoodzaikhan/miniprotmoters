package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.Utils.Event
import com.example.minipromoter.models.ProductModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//
// Created by Abdul Basit on 3/12/2020.
//

class AddNewProductDialogViewModel : BaseViewModel() {

    //coroutine scope so we can cancel the job if view model gets destroyed
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //live data bind to views
    val tittle = MutableLiveData("")
    val onButtonClicked = MutableLiveData<Event<Unit>>()

    //on button click function which triggers from view
    fun onAddClicked() {
        onButtonClicked.value = Event(Unit)
    }

    // function to add new product into db
    fun addNewProduct() {
        coroutineScope.launch(Dispatchers.IO) {
            val productModel = ProductModel(productName = tittle.value!!)
            App.getUserRepository().database.productDao.insertProduct(productModel)
        }
    }

    // a factory call to return view model object
    class Factory :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNewProductDialogViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddNewProductDialogViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }


}