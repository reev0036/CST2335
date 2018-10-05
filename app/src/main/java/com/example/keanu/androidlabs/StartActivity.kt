package com.example.keanu.androidlabs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button

class StartActivity : Activity() {

    val ACTIVITY_NAME = "StartActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        var button = findViewById<Button>(R.id.button1
        )

        button.setOnClickListener{
            var intent = Intent(this@StartActivity,ListItemsActivity::class.java)
            startActivityForResult(intent, 50)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 50)
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult")

    }

    override fun onResume() {
        Log.i(ACTIVITY_NAME, "In onResume()")
        super.onResume()

    }

    override fun onStart() {
        Log.i(ACTIVITY_NAME, "In onStart()")
        super.onStart()

    }

    override fun onPause() {
        Log.i(ACTIVITY_NAME, "In onPause()")
        super.onPause()

    }

    override fun onStop() {
        Log.i(ACTIVITY_NAME, "In onStop")
        super.onStop()

    }

    override fun onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy")
        super.onDestroy()

    }
}