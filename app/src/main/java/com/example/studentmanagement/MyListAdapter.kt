package com.example.studentmanagement

import Student
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyListAdapter(private var studentList : List<Student>): RecyclerView.Adapter<MyListAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    var isLinearLayout : Boolean  = true

    interface OnItemClickListener {
        fun onItemClick(i: Int)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val titleText = listItemView.findViewById<TextView>(R.id.name)
        val subtitleText = listItemView.findViewById<TextView>(R.id.classes)
        val detailsText = listItemView.findViewById<TextView>(R.id.details)
        val genderText = listItemView.findViewById<TextView>(R.id.gender)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MyListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView: View
        if (viewType == 0) {
            contactView = inflater.inflate(R.layout.my_list_item, parent, false)
        }
        else {
            contactView = inflater.inflate(R.layout.my_grid_layout, parent, false)
        }
        return ViewHolder(contactView)
    }
    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(isLinearLayout) {
            0
        } else {
            1
        }
    }
    fun switchViewType(): Boolean {
        isLinearLayout = !isLinearLayout
        return isLinearLayout
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student: Student = studentList.get(position)
        val titleText = holder.titleText
        titleText.setText(student.stuname)
        val subtitleText = holder.subtitleText
        subtitleText.setText(student.stuclasses)
        val detailsText = holder.detailsText
        detailsText.setText(student.stubirth)
        val genderText = holder.genderText
        genderText.setText(student.stugender)
        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }
    fun updateList(newList: List<Student>) {
        studentList = newList
    }
}