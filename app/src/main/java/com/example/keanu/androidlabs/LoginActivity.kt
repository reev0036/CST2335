package com.example.keanu.androidlabs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class LoginActivity : Activity() {

    val ACTIVITY_NAME = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var button = findViewById<Button>(R.id.loginButton)
        var emailInput = findViewById<EditText>(R.id.emailInput)

        var prefs = getSharedPreferences("myData", Context.MODE_PRIVATE)

        var defaultText = prefs.getString("emailAddress","email@domain.com")

        emailInput.setText(defaultText)

        button.setOnClickListener {
            var text = emailInput.getText().toString()
            var editor = prefs.edit()
            editor.putString("emailAddress",text)
            editor.commit()
            var intent = Intent(this@LoginActivity,StartActivity::class.java)

            startActivity(intent)
        }


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