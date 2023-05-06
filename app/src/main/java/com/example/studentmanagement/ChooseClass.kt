package com.example.studentmanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChooseClass : AppCompatActivity() {
    var key: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_class)
        val classList = listOf("20KTPM1", "20KTPM2", "20KTPM3", "20KTPM4")
        val avaList = listOf(R.drawable.ava1, R.drawable.ava1, R.drawable.ava1, R.drawable.ava1)
        var adapter = ClassListAdapter(this, classList, avaList)
        var simpleListView = findViewById<ListView>(R.id.simpleListView)
        simpleListView.adapter = adapter
        simpleListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val selected = parent.getItemAtPosition(i) as? String
            key = selected.toString()
            val replyIntent = Intent()
            replyIntent.putExtra("selectedData", key)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }
}

