package com.example.minipromoter.jobschedular

import android.app.Activity
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.example.minipromoter.App
import com.example.minipromoter.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber


//
// Created by Abdul Basit on 5/30/2020.
//

class SendMessagesToUser : JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        val userRepository = App.getUserRepository()

        Timber.d("Sending SMS started")

        startSendingSMS(userRepository)

        return false
    }

    private fun startSendingSMS(userRepository: UserRepository) {

        val getDataJob = GlobalScope.async(Dispatchers.IO) {
            userRepository.database.pendingUserOutgoingMessageDao.getFiveOutgoingUserMessagePriorityHigh()
        }

        getDataJob.invokeOnCompletion {
            if (it != null) {
                Timber.d("some issue occured")

            } else {
                val response = getDataJob.getCompleted()
                if (!response.isNullOrEmpty()) {

                    GlobalScope.launch(Dispatchers.IO) {
                        response.forEach {
                            sendMessage(it.message!!, it.sendTo!!)

                            userRepository.database.pendingUserOutgoingMessageDao.deleteUserOutgoingMessage(
                                it
                            )
                        }
                    }
                } else {

                    //cancelling the job scheduler
                    val jobScheduler =
                        this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                    jobScheduler.cancelAll()

                    Timber.d("Scheduler canceled")
                }
            }
        }


    }

    private fun sendMessage(message: String, toNumber: String) {
        val SENT = "SMS_SENT"
        val sentPI = PendingIntent.getBroadcast(applicationContext, 0, Intent(SENT), 0)

        //callback if message was send successfully or not
        this.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(
                            applicationContext,
                            "SMS sent",
                            Toast.LENGTH_LONG
                        ).show()

                        Timber.d("Successfully message was send")
                    }
                    else -> {
                        Timber.d("There was some issue")

                    }
                }
            }
        }, IntentFilter(SENT))


        //sending the sms
        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(toNumber, null, message, sentPI, null)
    }
}