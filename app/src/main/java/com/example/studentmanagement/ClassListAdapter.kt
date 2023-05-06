package com.example.studentmanagement

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ClassListAdapter (
    private val context: AppCompatActivity, private val classList: List<String>, private val avaList: List<Int>
    ) : ArrayAdapter<String>(context, R.layout.my_list_class, classList) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context.layoutInflater
            val rowView: View = inflater.inflate(R.layout.my_list_class, null, true)
            val titleText = rowView.findViewById<TextView>(R.id.nameClass)
            val imageView: ImageView = rowView.findViewById<ImageView>(R.id.avaClass)
            titleText.text = classList[position]
            imageView.setImageResource(avaList[position])
            return rowView
        }
}