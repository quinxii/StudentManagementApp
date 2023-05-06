package com.example.studentmanagement

import Student
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.view.MotionEvent
import android.view.View
import android.widget.*

@Suppress("DEPRECATION")
class Update : AppCompatActivity() {
    var key: String = ""
    val classList = listOf("20KTPM1", "20KTPM2", "20KTPM3", "20KTPM4")

    private var REQUEST_CODE = 1111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val spinner: Spinner = findViewById(R.id.etSpinner)
        var adapter = ArrayAdapter(this, R.layout.my_list_class, R.id.nameClass, classList)
            .also { adapter ->
                adapter.setDropDownViewResource(
                    R.layout.my_list_class)
                spinner.adapter = adapter
            }
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as? String
                key = selectedItem.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })
        val getStudent = intent.getParcelableExtra("myInfo") as? Student
        val getPos = intent.getIntExtra("myPos",-1) as? Int
        val name: String = getStudent!!.stuname
        val etName = findViewById<EditText>(R.id.etName)
        val birth: String = getStudent!!.stubirth
        val etBirth = findViewById<EditText>(R.id.etBirth)
        val classes: String = getStudent!!.stuclasses
        val selectedClass: Int = adapter.getPosition(classes)
        spinner.setSelection(selectedClass)
        val gender: String = getStudent!!.stugender
        val etGender = findViewById<RadioGroup>(R.id.editGroup)

        etName.setText(name)
        key = classes

        etBirth.setText(birth)
        var genderChoice: RadioButton
        for (i in 0 until etGender.childCount) {
            genderChoice = etGender.getChildAt(i) as RadioButton
            genderChoice.isChecked = true
            if(genderChoice.text.toString() == gender){
                break
            }
        }
        var btnUpdate = findViewById<Button>(R.id.btnUpdate)
        btnUpdate.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra("updateInfo","update")
            replyIntent.putExtra("getPos",getPos)
            val selected: Int = etGender.checkedRadioButtonId
            var gender: String = ""
            if(selected!=-1) {
                gender = findViewById<RadioButton>(selected).text.toString()
            }
            getStudent.stuname = etName.text.toString()
            getStudent.stubirth = etBirth.text.toString()
            getStudent.stuclasses = key
            getStudent.stugender = gender
            replyIntent.putExtra("updateStudent",getStudent)
            setResult(Activity.RESULT_OK,replyIntent)
            finish()
        }
        var btnDelete = findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra("updateInfo", "delete")
            replyIntent.putExtra("getPos", getPos)
            replyIntent.putExtra("studentInfo", getStudent)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val spinner: Spinner = findViewById(R.id.etSpinner)
            val selectedData = data?.getSerializableExtra("selectedData") as? String
            if (selectedData != null) {
                val index = classList.indexOf(selectedData)
                spinner.setSelection(index)
            }
        }
    }
}