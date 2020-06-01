package com.example.minipromoter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.models.ToolbarModel

//
// Created by Abdul Basit on 2/19/2020.
// Copyright (c) 2020 VisionX. All rights reserved.
//

class UserViewModel(private val productName: String) : BaseViewModel() {

   // val userList = App.getUserRepository().getProductUsersList()
    val toolbarModel = ToolbarModel(productName)

    //factory class to create view model instance
    class Factory(private val productName: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(productName) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}
