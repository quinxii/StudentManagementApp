package com.example.studentmanagement

import Student
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


@Suppress("DEPRECATION")
class Insert : AppCompatActivity() {
    var key: String = ""
    private val REQUEST_CODE = 111
    val classList = listOf("20KTPM1", "20KTPM2", "20KTPM3", "20KTPM4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        val classList = listOf("20KTPM1", "20KTPM2", "20KTPM3", "20KTPM4")
            val spinner: Spinner = findViewById(R.id.contactSpinner)
            ArrayAdapter(this, R.layout.my_list_class, R.id.nameClass, classList)
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
                val selectedClass = parent.getItemAtPosition(position) as? Int
                val selectedItem = parent.getItemAtPosition(position) as? String
                key = selectedItem.toString()
                if (selectedClass != null) {
                    spinner.setSelection(selectedClass)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })

        val etName = findViewById<EditText>(R.id.etName)
        val etBirth = findViewById<EditText>(R.id.etBirth)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        val btnInsert = findViewById<Button>(R.id.btnInsert)
        btnInsert.setOnClickListener {
            val etName: String = etName.text.toString()
            val etBirth: String = etBirth.text.toString()
            val selected: Int = radioGroup.checkedRadioButtonId
            var gender: String = ""
            if (selected != -1) {
                gender = findViewById<RadioButton>(selected).text.toString()
            }
            val replyIntent = Intent()
            val newStudent = Student()
            newStudent.stuname = etName
            println(newStudent.stuname)
            newStudent.stubirth = etBirth
            println(newStudent.stubirth)
            newStudent.stuclasses = key
            println(newStudent.stuclasses)
            newStudent.stugender = gender
            println(newStudent.stugender)

            if (newStudent.stuname.isNullOrEmpty() || newStudent.stubirth.isNullOrEmpty() || newStudent.stuclasses.isNullOrEmpty() || newStudent.stugender.isNullOrEmpty()) {
                Toast.makeText(this,"Please fill all the fields!", Toast.LENGTH_SHORT).show()
            }
            else {
//                replyIntent.putExtra("selectedItemPosition", key)
                replyIntent.putExtra("addStudent",newStudent)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spinner: Spinner = findViewById(R.id.etSpinner)
            val selectedData = data?.getSerializableExtra("selectedData") as? String
            if (selectedData != null) {
                val index = classList.indexOf(selectedData)
                spinner.setSelection(index)
            }
        }
    }
}



