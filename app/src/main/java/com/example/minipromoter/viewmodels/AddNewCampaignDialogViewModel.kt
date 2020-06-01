package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.Utils.Event
import com.example.minipromoter.models.Campaign
import com.example.minipromoter.models.Keywords
import com.example.minipromoter.models.ProductModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

//
// Created by Abdul Basit on 3/12/2020.
//

class AddNewCampaignDialogViewModel(val productModel: ProductModel) : BaseViewModel() {

    //coroutine scope so we can cancel the job if view model is destroyed
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    // mutable live data which are bind to view through 2 way data binding
    val tittle = MutableLiveData("")
    val message = MutableLiveData("")
    val expireMessage = MutableLiveData("")
    val isPolls = MutableLiveData(false)
    val option1 = MutableLiveData("")
    val option2 = MutableLiveData("")

    // event type data so we can send events to fragment
    val onButtonClicked = MutableLiveData<Event<Unit>>()


    fun onAddClicked() {
        onButtonClicked.value = Event(Unit)
    }

    //function to add new campaign
    fun addNewCampaign() {
        coroutineScope.launch(Dispatchers.IO) {
            val campaign =
                Campaign(
                    productId = productModel.productId,
                    campaignMessage = message.value,
                    campaignTittle = tittle.value?.trim(),
                    expiryAutoMessageReply = expireMessage.value


                )
            val campaignId = App.getUserRepository().database.campaignDao.insertCampaign(campaign)
            insertPrimaryKeywords(campaignId, campaign)


        }
    }

    //function to insert the default keywords in keywords table
    private fun insertPrimaryKeywords(campaignId: Long, campaign: Campaign) {

        val subKeyword =
            Keywords(
                inviteMessage = "<Sub>Sub ${productModel.productName}",
                description = "For Subscription",
                campaignId = campaignId
            )
        val unSubKeyword =
            Keywords(
                inviteMessage = "<sub>UnSub ${productModel.productName}",
                description = "For Unsubscribe",
                campaignId = campaignId
            )


        // adding those models into db
        App.getUserRepository().database.keywordsDao.insertKeywords(subKeyword)
        App.getUserRepository().database.keywordsDao.insertKeywords(unSubKeyword)

        if (isPolls.value!!) {

            val option1Keywords =
                Keywords(
                    inviteMessage = "<Vote>${campaign.campaignTittle}#1",
                    description = option1.value,
                    isOption = true,
                    campaignId = campaignId
                )
            val option2Keywords =
                Keywords(
                    inviteMessage = "<Vote>${campaign.campaignTittle}#2",
                    description = option2.value,
                    isOption = true,
                    campaignId = campaignId
                )
            App.getUserRepository().database.keywordsDao.insertKeywords(option1Keywords)
            App.getUserRepository().database.keywordsDao.insertKeywords(option2Keywords)

            val message = "Vote\n " +
                    option1Keywords.description + " send " + option1Keywords.inviteMessage + "\n" +
                    option2Keywords.description + " send " + option2Keywords.inviteMessage

            campaign.campaignMessage = campaign.campaignMessage + "\n" + message
            campaign.campaignId = campaignId
            App.getUserRepository().database.campaignDao.updateCampaign(campaign)


        }


    }


    class Factory(private val productModel: ProductModel) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNewCampaignDialogViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddNewCampaignDialogViewModel(productModel) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }


}