package com.example.readsms

import android.content.Intent

import android.os.IBinder

import android.content.pm.ResolveInfo

import android.content.IntentFilter
import android.util.Log

import android.os.Build

import android.R
import android.app.*

import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class SmsService : Service() {

    private var mSMSreceiver: SMSBroadcastReceiver? = null
    private var mIntentFilter: IntentFilter? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("Shabana", "Communicator started")
        //SMS event receiver
        mSMSreceiver = SMSBroadcastReceiver()
        mIntentFilter = IntentFilter()
        mIntentFilter!!.addAction("android.provider.Telephony.SMS_RECEIVED")
        mIntentFilter!!.priority = 1000
        registerReceiver(mSMSreceiver, mIntentFilter)
    }

    override fun onDestroy() {
        //super.onDestroy()
        val serviceIntent = Intent(this, SmsService::class.java)
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
        ContextCompat.startForegroundService(this, serviceIntent)
        // Unregister the SMS receiver
        unregisterReceiver(mSMSreceiver)

    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, "120")
            .setContentTitle("Foreground Service")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        //do heavy work on a background thread
        //stopSelf();
        return START_STICKY
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "120",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
}