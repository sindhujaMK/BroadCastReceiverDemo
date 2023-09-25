package com.example.broadcastreceiverdemo

/*import android.app.*
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
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class MyService : Service() {
    private var manager: NotificationManager? = null
    var notificationChannel: NotificationChannel? = null
    private val NOTIF_ID = 1
    private var builder: NotificationCompat.Builder? = null
    private var notificationLayout: RemoteViews? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("MyService", "onStartCommand")
        // do not receive all available system information (it is a filter!)
        startForegroundService(this)
        val battChangeFilter = IntentFilter(
            Intent.ACTION_BATTERY_CHANGED
        )
        // register our receiver
        this.registerReceiver(batteryChangeReceiver, battChangeFilter)
        return super.onStartCommand(intent, flags, startId)
    }

    private val batteryChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkBatteryLevel(intent, context)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        // There are Bound an Unbound Services - you should read about the differences. This one is an unbound one.
        return null
    }

    private fun checkBatteryLevel(batteryChangeIntent: Intent, context: Context) {
        // some calculations
        val currLevel = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val maxLevel = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val percentage = Math.round(currLevel * 100.0 / maxLevel).toInt()
        Log.d("MyService", "current battery level: $percentage")
        val status = batteryChangeIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        updateNotification(percentage, isCharging)
    }

    private fun startForegroundService(context: Context) {
        val notificationChannelId = packageName
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (manager == null) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                notificationChannelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ) // Make the notification low priority.
            // Configure the notification channel.
            notificationChannel!!.description = "scheduled_notification"
            notificationChannel!!.enableLights(true)
            notificationChannel!!.lockscreenVisibility = Notification.VISIBILITY_SECRET
            notificationChannel!!.lightColor = Color.BLUE
            notificationChannel!!.enableVibration(true)
            notificationChannel!!.vibrationPattern = longArrayOf(0L)
            notificationChannel!!.setShowBadge(true)
            manager!!.createNotificationChannel(notificationChannel!!)
        }

        // Create notification builder.
        builder = NotificationCompat.Builder(this, notificationChannelId)
        builder!!.setSmallIcon(R.mipmap.ic_launcher)
        builder!!.setVibrate(longArrayOf(0L))
        // Make the notification low priority.
        builder!!.priority = Notification.PRIORITY_DEFAULT
        builder!!.setFullScreenIntent(pendingIntent, true)
        builder!!.color = ContextCompat.getColor(this, R.color.teal_200)
        builder!!.setVisibility(NotificationCompat.VISIBILITY_SECRET)
        builder!!.setStyle(NotificationCompat.DecoratedCustomViewStyle())
        builder!!.setWhen(0)
        notificationLayout = RemoteViews(context.packageName, R.layout.notification_layout)
        //        notificationLayout.setOnClickPendingIntent(ivContactIconBg, pendingScreenLockIntent);
//        builder.setDeleteIntent(pendingScreenLockIntent);
        builder!!.setCustomContentView(notificationLayout)
        builder!!.setCustomBigContentView(notificationLayout)
        //        changeViewAndSetActions(context);
        // Start foreground service.
        startForeground(NOTIF_ID, builder!!.build())
    }

    private fun updateNotification(percentage: Int, isCharging: Boolean) {
        val api = Build.VERSION.SDK_INT
        // update the icon
        //notificationLayout!!.setImageViewResource(R.id.notif_icon, R.mipmap.ic_launcher)
        // update the title
        notificationLayout!!.setTextViewText(R.id.tvNotification, percentage.toString())
        // update the content
        notificationLayout!!.setTextViewText(R.id.tvSubNotification, getChargingState(isCharging))
        manager!!.notify(NOTIF_ID, builder!!.build())
    }

    private fun getChargingState(isCharging: Boolean): String {
        return if (isCharging) {
            "The device is charging"
        } else {
            "The device is not charging"
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
}*/





    /*private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(channelId , "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }*/





/*
package com.example.broadcastreceiverdemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat


class MyService : Service() {
    private val channelId  = "ForegroundService Kotlin"
    private lateinit var mRemoteViews : RemoteViews
    //private lateinit var mNotificationManager: NotificationManager



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notificationLayout = RemoteViews(packageName, R.layout.notification_layout)
        val notification = NotificationCompat.Builder(this, channelId )
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setCustomContentView(notificationLayout)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        implementBroadCastReceiver( )
        return START_NOT_STICKY
    }

    private fun implementBroadCastReceiver() {

//        val mNotificationManager = (NotificationManager) (Context.NOTIFICATION_SERVICE)
        val mBuilder = NotificationCompat.Builder(this, channelId )
        val api = Build.VERSION.SDK_INT
//        val mRemoteViews = RemoteViews(packageName, R.layout.notification_layout)

        // update the title
        mRemoteViews.setTextViewText(R.id.tvNotification, resources.getString(R.string.notifications))
        // update the content
        mRemoteViews.setTextViewText(
            R.id.tvSubNotification,
            resources.getString(R.string.chargings))
        // update the notification
        if (api < Build.VERSION_CODES.HONEYCOMB) {
            NotificationManager.notify(1, mBuilder)
        } else if (api >= Build.VERSION_CODES.HONEYCOMB) {
            NotificationManager.notify(1, mBuilder )
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(channelId , "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}

override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val batteryManager = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager

    val batteryLevel:Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    Toast.makeText(applicationContext, "Battery is $batteryLevel", Toast.LENGTH_SHORT).show()
    return START_STICKY
}

override fun onBind(p0: Intent?): IBinder? {
    return null
}

override fun onDestroy() {
    super.onDestroy()
}
*/







/* val batteryManager = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager

        val batteryLevel:Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        Toast.makeText(applicationContext, "Battery is $batteryLevel", Toast.LENGTH_SHORT).show()*/






