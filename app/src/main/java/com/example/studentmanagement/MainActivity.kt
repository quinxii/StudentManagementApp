package com.example.studentmanagement

import Student
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.indexOf
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import java.io.*
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    var studentList = ArrayList<Student>()
    var studentNames = ArrayList<String>()
    var autoCompleteTV: AutoCompleteTextView? = null
    var adapter = MyListAdapter(studentList)
    var checkAdapter: ArrayAdapter<String> ?= null
    var temp: String ?= null

    val REALM_STUDENT = "HD_REALM_NAME"
    lateinit var realm : Realm

    fun openDatabase(){
        val configuration = RealmConfiguration.Builder(
            setOf(Student::class))
            .name(REALM_STUDENT).build()

        realm = Realm.open(configuration)
        Log.i("OPEN_REALM_STUDENT",realm.configuration.path)
    }

    fun writPerson(student: Student){
        realm.writeBlocking {
            val managedPerson = this.copyToRealm(student)
            Log.i("WRITE_REALM_STUDENT","Wrote " + student.stuname)
        }
    }

    fun readAllPerSon(): RealmResults<Student>{
        val all = realm.query<Student>().find()
        Log.i("READ_REALM_STUDENT",all.toString())
        return all
    }

    fun readAllWithName(name: String): RealmResults<Student>{
        val all = realm.query<Student>().find()
        Log.i("hdlog","Read " + all.size + " elements")
        val personsByNameQuery = realm.query<Student>("stuname contains $0",
            name)
        val filteredPersons = personsByNameQuery.find()
        Log.i("hdlog","Contains " + filteredPersons.size + " with filter")
        Log.i("hdlog","Read " + filteredPersons.toString())
        return filteredPersons
    }

    fun readAndUpdate(id: io.realm.kotlin.types.ObjectId, newName: Student){
        val personsByNameQuery = realm.query<Student>("id == $0",
            id).find().first()
        realm.writeBlocking{
            findLatest(personsByNameQuery)?.stuname = newName.stuname
            findLatest(personsByNameQuery)?.stubirth = newName.stubirth
            findLatest(personsByNameQuery)?.stuclasses = newName.stuclasses
            findLatest(personsByNameQuery)?.stugender = newName.stugender
            Log.i("UPDATE_REALM_STUDENT",newName.toString())
        }
    }

    fun deletePersons(student: Student){
        realm.writeBlocking {
            val personsQuery = realm.query<Student>("id == $0", student.id)
            val results = personsQuery.find()
            results.forEach { delete(findLatest(it)!!) }
        }
    }

    private val REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openDatabase()
        readAllPerSon()
        val readDBResult : RealmResults<Student> = readAllPerSon()
        studentList.addAll(readDBResult.subList(0, readDBResult.size))
        for (i in studentList.indices) {
            studentNames.add(studentList[i].stuname)
        }
        val rvContacts = findViewById<RecyclerView>(R.id.contactsRV) as RecyclerView
        adapter = MyListAdapter(studentList)
        rvContacts.adapter = adapter
        rvContacts.layoutManager = LinearLayoutManager(this)
        val GridBtn = findViewById<ImageButton>(R.id.GridBtn)

        GridBtn.setOnClickListener {
            toggleLayout()
        }
//        for (student in studentList) {
//            studentNames.add(student.stuname)
//        }
        autoCompleteTV = findViewById(R.id.autoCompleteTextView)
        checkAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, studentNames)
        autoCompleteTV!!.setAdapter(checkAdapter)
        autoCompleteTV!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredList = studentList.filter { it.stuname == p0.toString() }
                if (filteredList.isNotEmpty()) {
                    var attribute: List<Student>
                    for (i in filteredList.indices) {
                        val selected = filteredList[i]
                        attribute = listOf(selected)
                        adapter.updateList(attribute)
                        adapter.setOnItemClickListener(object : MyListAdapter.OnItemClickListener {
                            override fun onItemClick(i: Int) {
                                val intent = Intent(this@MainActivity, Update::class.java)
                                intent.putExtra("myInfo", selected)
                                intent.putExtra("myPos", studentList.indexOf(selected))
                                startActivityForResult(intent, 1111)
                            }
                        })
                    }
                }
                else {
                    adapter.setOnItemClickListener(object : MyListAdapter.OnItemClickListener {
                        override fun onItemClick(i: Int) {
                            val selected = studentList[i]
                            val intent = Intent(this@MainActivity, Update::class.java)
                            intent.putExtra("myInfo", selected)
                            intent.putExtra("myPos", i)
                            startActivityForResult(intent, 1111)
                        }
                    })
                    adapter.updateList(studentList)
                }
                checkAdapter?.notifyDataSetChanged()
                rvContacts.adapter = adapter
                adapter?.notifyDataSetChanged()
            }
        })
        var btnChange = findViewById<Button>(R.id.btnChange)
        btnChange.setOnClickListener {
            val intent : Intent = Intent(this, Insert::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
        adapter.setOnItemClickListener(object : MyListAdapter.OnItemClickListener {
            override fun onItemClick(i: Int) {
                val selected = studentList[i]
                val intent = Intent(this@MainActivity, Update::class.java)
                intent.putExtra("myInfo", selected)
                intent.putExtra("myPos", i)
                startActivityForResult(intent, 1111)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val rvContacts = findViewById<RecyclerView>(R.id.contactsRV)

        if (requestCode === REQUEST_CODE && resultCode === Activity.RESULT_OK) {
            val reply = data!!.getParcelableExtra("addStudent") as? Student
            if(reply?.stuname.isNullOrEmpty() || reply?.stuclasses.isNullOrEmpty() || reply?.stubirth.isNullOrEmpty() || reply?.stugender.isNullOrEmpty() ) {
                Toast.makeText(this,"Please fill all the fields!", Toast.LENGTH_SHORT).show()
            }
            else {
                reply?.let {
                    writPerson(reply)
                    studentList.add(it)
                    studentNames.add(it.stuname)
                    checkAdapter!!.notifyDataSetChanged()
                }
                adapter.notifyItemInserted(adapter.itemCount)
                checkAdapter!!.clear()
                checkAdapter!!.addAll(studentNames)
                checkAdapter!!.notifyDataSetChanged()
                adapter.updateList(studentList)
                rvContacts.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
        else if (requestCode === 1111 && resultCode === Activity.RESULT_OK) {
            val update: String = data!!.getStringExtra("updateInfo").toString()
            val getPos: Int = data!!.getIntExtra("getPos",-1)
            if(update == "update") {
                var newStudent = data!!.getParcelableExtra("updateStudent") as? Student
                newStudent?.let { readAndUpdate(it.id, it) }
                studentList[getPos] = newStudent!!
                adapter.notifyItemChanged(getPos)
                checkAdapter!!.clear()
                checkAdapter!!.addAll(studentNames)
                checkAdapter!!.notifyDataSetChanged()
                autoCompleteTV!!.setText("")
            }
            else if (update == "delete") {
                val delStudent = data!!.getParcelableExtra("studentInfo") as? Student
                delStudent?.let { deletePersons(it) }
                studentList.removeAt(getPos)
                adapter.notifyItemRemoved(getPos)
                checkAdapter!!.clear()
                checkAdapter!!.addAll(studentNames)
                checkAdapter!!.notifyDataSetChanged()
                autoCompleteTV!!.setText("")
            }
        }
    }

    private fun toggleLayout() {
        val rvContacts = findViewById<RecyclerView>(R.id.contactsRV)
        val GridBtn = findViewById<ImageButton>(R.id.GridBtn)
        if (!adapter.switchViewType()) {
            rvContacts.layoutManager = GridLayoutManager(this, 2)
            GridBtn.setImageResource(android.R.drawable.ic_menu_sort_by_size)
        } else {
            rvContacts.layoutManager = LinearLayoutManager(this)
            GridBtn.setImageResource(android.R.drawable.ic_dialog_dialer)
        }
    }
}