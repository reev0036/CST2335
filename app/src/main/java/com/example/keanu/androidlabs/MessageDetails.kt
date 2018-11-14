package com.example.keanu.androidlabs

import android.app.Activity
import android.os.Bundle

class MessageDetails : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_details)

        var infoToPass = intent.extras

        var newFragment = MessageFragment()
        newFragment.arguments = infoToPass

        newFragment.amITablet = false

        var transition = fragmentManager.beginTransaction()
        transition.replace(R.id.fragment_location, newFragment)
        transition.commit()
    }
}
