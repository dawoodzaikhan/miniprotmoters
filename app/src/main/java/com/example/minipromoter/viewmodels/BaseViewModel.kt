package com.example.minipromoter.viewmodels

//
// Created by Abdul Basit on 2/17/2020.
// Copyright (c) 2020 VisionX. All rights reserved.
//


import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.minipromoter.App

open class BaseViewModel :
    AndroidViewModel(App.getInstance()) {

}