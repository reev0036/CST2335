package com.example.keanu.androidlabs

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.inputmethod.InputMethodManager

class ChatWindow : Activity() {

    val CREATION_STATEMENT = "CREATE TABLE MESSAGES ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " aMessage text )"
    val MESSAGE = "Hi hey hello"
    var numItems = 100
    var messages = ArrayList<String>()
    var messageClicked = 0

    lateinit var dhHelper : MyOpenHelper
    lateinit var db :SQLiteDatabase
    lateinit var results : Cursor
    lateinit var theAdapter : MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_window)
        dhHelper = MyOpenHelper()
        db = dhHelper.writableDatabase

        var myList = findViewById<ListView>(R.id.myList)
        var inputText = findViewById<EditText>(R.id.inputText)
        var button = findViewById<Button>(R.id.startChat)

        var fragmentLocation = findViewById<FrameLayout>(R.id.fragment_location)
        var amITablet = (fragmentLocation != null)

        myList.setOnItemClickListener { parent, view, position, id ->

            var infoToPass = Bundle()

            messageClicked = position

            infoToPass.putString("Message", messages.get(position))
            infoToPass.putLong("ID", id)

            if (amITablet)
            {
                /*
                var newFragment = MessageFragment()
                var loadFragment = getFragmentManager().beginTransaction()
                loadFragment.replace(R.id.fragment_location, newFragment)
                newFragment.arguments = infoToPass
                newFragment.parentDocument = this
                loadFragment.commit()
                */

                var newFragment = MessageFragment()
                newFragment.arguments = infoToPass

                newFragment.amITablet = true

                var transition = fragmentManager.beginTransaction()
                transition.replace(R.id.fragment_location, newFragment)
                transition.commit()

            }
            else {
                var detailActivity = Intent(this, MessageDetails::class.java)
                detailActivity.putExtras(infoToPass)
                startActivityForResult(detailActivity, 35)
            }
        }



        theAdapter = MyAdapter( this)

        var context = this

        results = db.query(TABLE_NAME, arrayOf(KEY_MESSAGES, KEY_ID),
                null, null, null, null, null, null
        )

        var numRows = results.getCount()
        var numColumn = results.columnCount
        results.moveToFirst()

        for (i in 0..numColumn-1){
            Log.i("ChatWindow:", results.getColumnName(i))
        }

        val keyIndex = results.getColumnIndex(KEY_MESSAGES)

        while(!results.isAfterLast)
        {
            var thisMessage = results.getString(keyIndex)
            messages.add(thisMessage) //preload from database
            results.moveToNext()
        }

        button.setOnClickListener(){
            var userTyped = inputText.getText().toString()
            messages.add(userTyped)
            var newRow = ContentValues()
            newRow.put(KEY_MESSAGES, userTyped)
            db.insert(TABLE_NAME, "", newRow)

            results = db.query(TABLE_NAME, arrayOf(KEY_MESSAGES, KEY_ID),
                    null, null, null, null, null, null
            )


            inputText.setText("")
            theAdapter.notifyDataSetChanged()

            val imn = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imn.hideSoftInputFromWindow(button.getWindowToken(),0)
        }
        myList?.setAdapter( theAdapter )
        //lab 5

    }

    fun deleteMessage(id:Long){
        db.delete(TABLE_NAME, "_id=$id", null)
        results = db.query(TABLE_NAME, arrayOf(KEY_MESSAGES, KEY_ID),
                null, null, null, null, null, null
        )
        messages.removeAt(messageClicked)
        theAdapter.notifyDataSetChanged()

    }



    inner class MyAdapter(ctx : Context) : ArrayAdapter<String>(ctx, 0 ) {

        override fun getCount(): Int{
            return messages.size
        }

        override fun getItem(position:Int) : String {
            return messages.get(position)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup) : View? {
            var inflater = LayoutInflater.from(parent.getContext())

            var thisRow = null as View?
            if (position % 2 == 0)
                thisRow = inflater.inflate(R.layout.chat_row_outgoing, null)
            else
                thisRow = inflater.inflate(R.layout.chat_row_incoming, null)

            var textView = thisRow.findViewById<TextView>(R.id.message_text)
            textView.setText( getItem(position) )

            return thisRow
        }

        override fun getItemId(position:Int) : Long {
            results.moveToPosition(position)
            return results.getInt(results.getColumnIndex("_id")).toLong()
        }

    }

    val KEY_ID = "_id"
    val TABLE_NAME = "ChatMessages"
    val KEY_MESSAGES = "Messages"
    val DATABASE_NAME = "Messages.db"
    val VERSION_NUM = 4


    inner class MyOpenHelper : SQLiteOpenHelper(this@ChatWindow, DATABASE_NAME, null, VERSION_NUM)
    {


        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE $TABLE_NAME ( _id INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_MESSAGES TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
            db.execSQL("DROP TABLE IF EXISTS  $TABLE_NAME") //delete all current data

            onCreate(db)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if((requestCode == 35) && (resultCode == Activity.RESULT_OK))
        {
            var num = (data!!.getLongExtra("ID", 0))
            deleteMessage(num)
        }
    }

}

