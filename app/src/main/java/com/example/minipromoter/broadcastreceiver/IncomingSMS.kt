package com.example.minipromoter.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import com.example.minipromoter.models.SubscribersProductsCrossRef
import com.example.minipromoter.models.UserMessage
import com.example.minipromoter.models.UserModel
import com.example.minipromoter.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

//
// Created by Abdul Basit on 3/8/2020.
//

class IncomingSMS : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        Log.d("IncomingSMS", "Hi, i am here")
        var str = ""

        // getting the incoming message details
        if (bundle != null) {
            var from = ""
            var smsBody = ""
            val pdus = bundle.get("pdus") as Array<*>
            for (onePdus: Any? in pdus) {
                val oneSMS = SmsMessage.createFromPdu(onePdus as ByteArray)
                str += "SMS from " + oneSMS.originatingAddress
                str += " :"
                str += oneSMS.messageBody.toString()
                str += "\n"
                from = oneSMS.originatingAddress!!
                smsBody = oneSMS.messageBody.toString()
            }
            runBlocking(Dispatchers.IO) {

                // processing the incoming message
                processTheMessage(from, smsBody)
            }
        }
    }


    private fun processTheMessage(sender: String, message: String) {
        val userRepository = UserRepository()

        if (message.contains("<Sub>", ignoreCase = true) || message.contains(
                "<Vote>",
                ignoreCase = true
            )
        ) {

            try {

                // getting the keywords against that message
                val keyword =
                    userRepository.database.keywordsDao.getKeywordByInviteMessage(message.toUpperCase())

                // checking if we have keyword like this
                if (keyword != null) {

                    // getting the user model
                    val userModel = checkForUser(sender, userRepository)

                    // getting the campaign model
                    val campaignModel =
                        userRepository.database.campaignDao.getCampaignById(keyword.campaignId)

                    //getting the product model
                    val productModel =
                        userRepository.database.productDao.getProductById(campaignModel.productId)


                    var productSubscribersList =
                        userRepository.database.productSubscribersDao.getProductCrossRef(
                            productModel.productId
                        )
                    var productSubscribers =
                        productSubscribersList.findLast { it.parentUserId == userModel!!.userId }

                    Log.d(
                        "Incoming Message",
                        "User $productSubscribers"
                    )
                    Log.d(
                        "Incoming Message",
                        "User ID ${userModel?.userId} and product ID ${productModel.productId}"
                    )
                    Log.d(
                        "Incoming Message",
                        "Incoming message details ${userRepository.database.productSubscribersDao.getAllProductSubscribers()}"
                    )

                    // checking if we have that user subscribed, if not then add otherwise ignore
                    if (productSubscribers == null) {
                        productSubscribers =
                            SubscribersProductsCrossRef(
                                parentUserId = userModel!!.userId,
                                parentProductId = productModel.productId,
                                isActive = true
                            )
                        val productSubscribersId =
                            userRepository.database.productSubscribersDao.insert(productSubscribers)
                        productSubscribers.id = productSubscribersId
                    }

                    if (message.contains("<sub>", ignoreCase = true)) {
                        if (message.contains("unsub", ignoreCase = true)) {
                            updateProductSubscription(false, productSubscribers, userRepository)

                        } else if (message.contains("sub", ignoreCase = true)) {
                            updateProductSubscription(true, productSubscribers, userRepository)

                        }
                    } else if (message.contains("<Vote>", ignoreCase = true)) {
                        keyword.count = keyword.count + 1
                        userRepository.database.keywordsDao.updateKeywords(keyword)
                    }


                    val userMessage =
                        UserMessage(
                            userId = userModel!!.userId,
                            message = message,
                            isIncomingMessage = true
                        )
                    userRepository.database.userMessageDao.insertUserMessage(userMessage)


                } else {
                    //send message of unformatted text
                    sendMessage("Unable to process the message", sender)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendMessage("Unable to process the message", sender)
            }
        } else {
/*
            val userModel = userRepository.database.userDao.findUserByPhoneNumber(sender)
            userModel?.let {
                val userMessage =
                    UserMessage(
                        userId = userModel.userId,
                        message = message,
                        isIncomingMessage = true
                    )
                userRepository.database.userMessageDao.insertUserMessage(userMessage)
            }*/
        }
    }

    private fun checkForUser(userNumber: String, userRepository: UserRepository): UserModel? {

        // getting the user model
        var userModel = userRepository.database.userDao.findUserByPhoneNumber(userNumber)

        return if (userModel == null) {
            userModel = UserModel(phoneNumber = userNumber)
            val userID = userRepository.database.userDao.insertUser(userModel)
            userModel.userId = userID
            userModel
        } else {
            userModel
        }
    }

    private fun updateProductSubscription(
        isActive: Boolean,
        produceSubscribersProductsCrossRef: SubscribersProductsCrossRef,
        userRepository: UserRepository
    ) {
        produceSubscribersProductsCrossRef.isActive = isActive
        userRepository.database.productSubscribersDao.update(produceSubscribersProductsCrossRef)
    }

    private fun sendMessage(message: String, number: String) {
        //sending the sms
        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(number, null, message, null, null)

    }
}


