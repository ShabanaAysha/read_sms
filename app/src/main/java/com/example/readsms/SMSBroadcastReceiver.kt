package com.example.readsms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.provider.Telephony.Sms

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri


class SMSBroadcastReceiver : BroadcastReceiver() {
   val sharedPreferences: SharedPreferences?=null

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("shabana","SmS RECEIVED")
        val data = intent!!.extras
        var new = ""
        val time = System.currentTimeMillis()
        var formatter =  SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val dateString = formatter.format(Date(time))

        val pdus = data!!["pdus"] as Array<Any>?
        for (i in pdus!!.indices) {
            val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdus!![i] as ByteArray)

            val message = "Sender : " + smsMessage.getDisplayOriginatingAddress()
                .toString() + " \nMessage : " + smsMessage.getDisplayMessageBody()
                .toString() + " \nTime : "
            Log.d("shabana",message)
             new = new + smsMessage.messageBody.replace("\n","")


        }
        sendSms(new+"\n Time "+dateString )


    }

    private fun sendSms(message: String){
        try {
            Log.d("shabana sendingMessage",message)

            val smsManager: SmsManager = SmsManager.getSmsManagerForSubscriptionId(SmsManager.getDefaultSmsSubscriptionId())
            val length: Int = message.length

            if (length > 160) {
                val messagelist: ArrayList<String> = smsManager.divideMessage(message)
                smsManager.sendMultipartTextMessage(ReadSmsConstants.mobileNumber, null, messagelist, null, null)
            } else {
                smsManager.sendTextMessage(ReadSmsConstants.mobileNumber, null, message, null, null)
            }
            smsManager.sendTextMessage(ReadSmsConstants.mobileNumber, null, message, null, null)
            Log.d("shabana sendingMessage1",message)

        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("shabana sendingMessageFailed",message)

        }
    }

}