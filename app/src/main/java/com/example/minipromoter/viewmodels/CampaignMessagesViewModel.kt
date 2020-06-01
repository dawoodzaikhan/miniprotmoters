package com.example.minipromoter.viewmodels

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.jobschedular.SendMessagesToUser
import com.example.minipromoter.models.Campaign
import com.example.minipromoter.models.CampaignMessages
import com.example.minipromoter.models.PendingUserOutgoingMessages
import com.example.minipromoter.models.UserMessage
import kotlinx.coroutines.*
import timber.log.Timber


class CampaignMessagesViewModel(val model: Campaign) : ViewModel() {

    //coroutine scope
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // getting keywords list from db
    val keywords =
        App.getUserRepository().database.keywordsDao.getCampaignKeywords(model.campaignId)
    var optionKeywords =
        App.getUserRepository().database.keywordsDao.getAllOptionKeyword(model.campaignId)

    var optionKeywordsSize = MutableLiveData(1)

    // getting camping messages list from db
    val campaignMessage =
        App.getUserRepository().database.campaignMessageDao.getCampaignMessages(model.campaignId)

    //getting product subscribers
    val productSubscribers =
        App.getUserRepository().database.productSubscribersDao.getProductSubscribers(model.productId)

    // hashmap to save phone number and message table id
    private val hashmapWithPhoneAndSMSID = HashMap<String, Long>()


    //function which triggers on floating action button click
    fun startSendingMessage() {
        insertMessageIntoCampaignMessages()
    }

    // if message is successfully send to that number so we update our flag that message was send successfully
    fun messageSuccessfullySend(phoneNumber: String) {
        GlobalScope.launch {
            val messageId = hashmapWithPhoneAndSMSID[phoneNumber]
            val userMessage =
                App.getUserRepository().database.userMessageDao.getUserLastMessage(messageId!!)

            //updating the flag
            userMessage.isSuccessfullySend = true

            //updating in db
            App.getUserRepository().database.userMessageDao.updateUserMessage(userMessage)

        }
    }

    private fun insertMessageIntoCampaignMessages() {

        //insert into campaign message
        coroutineScope.launch(Dispatchers.IO) {
            val campaignMessage =
                CampaignMessages(
                    message = model.campaignMessage,
                    campaignId = model.campaignId
                )
            App.getUserRepository().database.campaignMessageDao.insertCampaignMessage(
                campaignMessage
            )
        }

        //insert into user message
        coroutineScope.launch(Dispatchers.IO) {
            productSubscribers.value?.forEach {
                val messageId = App.getUserRepository().database.userMessageDao.insertUserMessage(
                    UserMessage(
                        message = model.campaignMessage,
                        userId = it.userId
                    )
                )
                hashmapWithPhoneAndSMSID[it.phoneNumber!!] = messageId
            }
        }

        //inserting into pending messages
        coroutineScope.launch(Dispatchers.IO) {
            productSubscribers.value?.forEach {
                val pendingOutgoingMessage = PendingUserOutgoingMessages(
                    outgoingMessageUserId = it.userId,
                    message = model.campaignMessage,
                    sendTo = it.phoneNumber

                )
                App.getUserRepository().database.pendingUserOutgoingMessageDao.insert(
                    pendingOutgoingMessage
                )
            }

            //starting the job scheduler
            startPolling(App.getInstance())
        }
    }

    private fun startPolling(context: Context) {
        val scheduler =
            context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val JOB_ID = 1
        Timber.d("scheduler.schedule(jobInfo)")
        val jobInfo = JobInfo.Builder(
            JOB_ID, ComponentName(context, SendMessagesToUser::class.java)
        )
            .setPeriodic(900000)
            .setPersisted(true)
            .build()
        scheduler.schedule(jobInfo)

    }


    //factory to get the view model
    class Factory(val model: Campaign) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CampaignMessagesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CampaignMessagesViewModel(model) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}
