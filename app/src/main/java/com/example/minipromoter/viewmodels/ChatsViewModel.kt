package com.example.minipromoter.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.models.ProductModel
import com.example.minipromoter.models.UserMessage
import com.example.minipromoter.models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatsViewModel(private val userModel: UserModel) : ViewModel() {

    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val userMessages =
        App.getUserRepository().database.userMessageDao.getAllMessages()//g(userModel.userId)
    val message = MutableLiveData("")


    fun addUserMessage() {
        coroutineScope.launch(Dispatchers.IO) {
            val userMessage = UserMessage(
                userId = userModel.userId,
                message = message.value,
                isIncomingMessage = false
            )
            val messageId =
                App.getUserRepository().database.userMessageDao.insertUserMessage(userMessage)
            Log.d("sf", "User id : $messageId")
        }
    }


    class Factory(private val userModel: UserModel) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChatsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChatsViewModel(userModel) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}
