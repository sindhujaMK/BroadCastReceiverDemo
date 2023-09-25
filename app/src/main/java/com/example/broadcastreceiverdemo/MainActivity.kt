package com.example.broadcastreceiverdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var start: Button? = null
    private var stop: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start = findViewById<View>(R.id.btnStartService) as Button

        stop = findViewById<View>(R.id.btnStopService) as Button

        start!!.setOnClickListener(this)
        stop!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        if (view === start) {
            val startIntent = Intent(this, MyService::class.java)
            startService(startIntent)
        } else if (view === stop) {
            val stopIntent = Intent(this, MyService::class.java)
            stopService(stopIntent)
        }
    }
}


