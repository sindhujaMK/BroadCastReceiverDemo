package com.example.broadcastreceiverdemo

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlin.math.roundToInt

class MyService : Service() {
    private var mBuilder: NotificationCompat.Builder? = null
    private var mNotificationManager: NotificationManager? = null
    private var mRemoteViews: RemoteViews? = null
    private var mNotificationChannel: NotificationChannel? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*createNotificationChannel()*/
        val notificationIntent = Intent(this, SecondActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        startForegroundService()

        val battChangeFilter = IntentFilter(
            Intent.ACTION_BATTERY_CHANGED
        )
        this.registerReceiver(batteryChangeReceiver, battChangeFilter)

         mRemoteViews = RemoteViews(packageName, R.layout.notification_layout)
         mBuilder = NotificationCompat.Builder(this, packageName )
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setVibrate(longArrayOf(0L))
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setFullScreenIntent(pendingIntent, true)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setWhen(0)
             .setContentIntent(pendingIntent)
            .setCustomContentView(mRemoteViews)
            .setCustomBigContentView(mRemoteViews)
        startForeground(1, mBuilder!!.build())

        return START_NOT_STICKY
        //return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private val batteryChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            implementChargeLevel(intent)
        }
    }

    private fun implementChargeLevel(batteryChangeIntent: Intent) {
        val currLevel = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val maxLevel = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val percentage = (currLevel * 100.0 / maxLevel).roundToInt()
        Log.e("TAG", "current battery level: $percentage")
        val status = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        updateNotification(percentage, isCharging)
    }

    private fun updateNotification(percentage: Int, isCharging: Boolean) {
        Build.VERSION.SDK_INT
        mRemoteViews!!.setTextViewText(R.id.tvPercentage,  resources.getString(R.string.percentage))
        mRemoteViews!!.setTextViewText(R.id.tvNotification, percentage.toString())
        mRemoteViews!!.setTextViewText(
            R.id.tvSubNotification, getChargingState(isCharging))
        mNotificationManager!!.notify(1, mBuilder!!.build())
    }

    private fun getChargingState(isCharging: Boolean): CharSequence? {
        return if (isCharging) {
            "The device is charging"
        } else {
            "The device is not charging"
        }
    }

    private fun startForegroundService() {
        val notificationChannelId = packageName
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (mNotificationManager == null) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationChannel = NotificationChannel(
                notificationChannelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotificationChannel!!.description = "Scheduled Notification"
            mNotificationChannel!!.enableLights(true)
            mNotificationChannel!!.lockscreenVisibility = Notification.VISIBILITY_SECRET
            mNotificationChannel!!.lightColor = Color.GREEN
            mNotificationChannel!!.enableVibration(true)
            mNotificationChannel!!.vibrationPattern = longArrayOf(0L)
            mNotificationChannel!!.setShowBadge(true)
            mNotificationManager!!.createNotificationChannel(mNotificationChannel!!)
        }
    }
    override fun onDestroy() {
        try {
            unregisterReceiver(batteryChangeReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }
}




/* mBuilder = NotificationCompat.Builder(this, packageName)
        mBuilder!!.setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setVibrate(longArrayOf(0L))
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setFullScreenIntent(pendingIntent, true)
        mBuilder!!.color = ContextCompat.getColor(this, R.color.teal_200)
        mBuilder!!.setVisibility(NotificationCompat.VISIBILITY_SECRET)
        mBuilder!!.setStyle(NotificationCompat.DecoratedCustomViewStyle())
        mBuilder!!.setWhen(0)
        mRemoteViews = RemoteViews(packageName, R.layout.notification_layout)
        mBuilder!!.setCustomContentView(mRemoteViews)
        mBuilder!!.setCustomBigContentView(mRemoteViews)
        // Start foreground service.
        startForeground(1, mBuilder!!.build())*/