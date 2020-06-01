package com.example.minipromoter.fragment

import android.app.Activity
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.minipromoter.adapter.CampaignMessageOnClickListener
import com.example.minipromoter.adapter.CampaignMessagesAdapter
import com.example.minipromoter.adapter.KeywordsAdapter
import com.example.minipromoter.adapter.KeywordsClickListner
import com.example.minipromoter.databinding.FragmentCampainMessagesBinding
import com.example.minipromoter.jobschedular.SendMessagesToUser
import com.example.minipromoter.viewmodels.CampaignMessagesViewModel
import kotlinx.android.synthetic.main.fragment_campain_messages.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


class CampaignMessagesFragment : Fragment() {

    //arguments from the fragments
    private val args: CampaignMessagesFragmentArgs by navArgs()

    // view model for fragment
    private val viewModel: CampaignMessagesViewModel by lazy {
        ViewModelProvider(
            this,
            CampaignMessagesViewModel.Factory(args.model)
        ).get(CampaignMessagesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCampainMessagesBinding.inflate(inflater)

        //assigning the view model and lifecycle
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //assigning adapter to recyclerview
        binding.rvKeywords.adapter = KeywordsAdapter(viewModel, KeywordsClickListner {

        })

        //observing the keywords live data so we can submit to adapter on data change
        viewModel.keywords.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvKeywords.adapter as KeywordsAdapter

                // submitting the data to adapter
                adapter.submitList(it)
            }
        })

        // assigning adapter to campaign messages recyclerview
        binding.rvCampaignMessages.adapter =
            CampaignMessagesAdapter(CampaignMessageOnClickListener {
            })

        // observing the campaign message live data so we can notify the adapter
        viewModel.campaignMessage.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val adapter = binding.rvCampaignMessages.adapter as CampaignMessagesAdapter
                adapter.submitList(it)
            }
        })


        viewModel.optionKeywords.observe(viewLifecycleOwner, Observer {
            var total = 0
            it.forEach {
                total += it.count
            }
            viewModel.optionKeywordsSize.value = total
            rvKeywords.adapter!!.notifyDataSetChanged()
        })

        viewModel.productSubscribers.observe(viewLifecycleOwner, Observer {
            Log.d("CampaignMessagesFragment", "Subscribers Size : " + it.size)
        })


        // click listener to floating button
        binding.fbAdd.setOnClickListener {
            viewModel.startSendingMessage()
           // sendMessage(viewModel.model.campaignMessage!!)
        }

        return binding.root
    }


    private fun sendMessage(message: String) {

        //sending message
        viewModel.productSubscribers.value?.forEach { userModel ->
            GlobalScope.launch(Dispatchers.IO) {

                val SENT = "SMS_SENT"
                val sentPI = PendingIntent.getBroadcast(context!!, 0, Intent(SENT), 0)

                //callback if message was send successfully or not
                activity!!.registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(arg0: Context, arg1: Intent) {
                        when (resultCode) {
                            Activity.RESULT_OK -> {
                                Toast.makeText(
                                    context!!,
                                    "SMS sent",
                                    Toast.LENGTH_LONG
                                ).show()
                                viewModel.messageSuccessfullySend(userModel.phoneNumber!!)

                            }
                            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                                context!!,
                                "Generic failure",
                                Toast.LENGTH_LONG
                            ).show()
                            SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                                context!!,
                                "No service",
                                Toast.LENGTH_LONG
                            ).show()
                            SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(
                                context!!,
                                "Null PDU",
                                Toast.LENGTH_LONG
                            ).show()
                            SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(
                                context!!,
                                "Radio off",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }, IntentFilter(SENT))


                //sending the sms
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(userModel.phoneNumber, null, message, sentPI, null)

            }
        }

    }



}
