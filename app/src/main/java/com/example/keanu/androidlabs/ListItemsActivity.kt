package com.example.keanu.androidlabs

import android.app.Activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*

class ListItemsActivity : Activity() {

    val ACTIVITY_NAME = "ListItemsActivity"
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(ACTIVITY_NAME, "In onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_items)

        var imageBtn = findViewById<ImageButton>(R.id.ImageButton20)

        imageBtn.setOnClickListener {
            val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(takePicIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        var switch = findViewById<Switch>(R.id.switch20)
        switch.setOnCheckedChangeListener { e, f ->
            if(switch.isChecked)
            {
                val text = "Switch On!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this,text,duration)
                toast.show()
            }
            else
            {
                val text = "Switch Off!"
                val duration = Toast.LENGTH_LONG
                val toast = Toast.makeText(this,text,duration)
                toast.show()
            }
        }

        var checkBox = findViewById<CheckBox>(R.id.checkBox20)

        checkBox.setOnCheckedChangeListener { e, f ->
            var builder = AlertDialog.Builder(this)
            builder.setMessage("")
                    .setTitle("Do you want to quit?")
                    .setPositiveButton("Ok") { dialog, id ->
                        val resultIntent = Intent()
                        resultIntent.putExtra("Response","This is my response")
                        setResult(Activity.RESULT_OK,resultIntent)
                        finish()
                    }
                    .setNegativeButton("Cancel") { dialog, id ->

                    }
                    .show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data.extras.get("data") as Bitmap
            var imageBtn = findViewById<ImageButton>(R.id.ImageButton20)
            imageBtn.setImageBitmap(imageBitmap)
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

