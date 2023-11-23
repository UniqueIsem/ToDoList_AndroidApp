package com.example.proyecto_todolist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_todolist.R
import com.example.proyecto_todolist.TodoItem

class MainActivity : AppCompatActivity() {
    private lateinit var tasks: MutableList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var editTextTask: EditText
    private lateinit var btnAddTask: Button
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tasks = getAllTasks()
        btnAddTask = findViewById(R.id.btnAddTask)
        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)
        todoAdapter =
            TodoAdapter(tasks, R.layout.item_todo, object : TodoAdapter.OnItemClickListener{
                override fun onItemclick(name: String?, position: Int) {
                    //Toast.makeText(this@MainActivity, name + " -"+ position, Toast.LENGTH_LONG).show()
                    deleteTask(position)
                }
            })
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setAdapter(todoAdapter)



        btnAddTask.setOnClickListener {
            val newTask = editTextTask.text.toString()
            if (newTask.isNotEmpty()) {
                addTask(0)
                todoAdapter.notifyDataSetChanged()
                editTextTask.text.clear()
            } else {
                Toast.makeText(this, "Ingrese un task...", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun getAllTasks(): MutableList<String> {
        return object : ArrayList<String>() {
            init {
                add("Barrer")
                add("Tender la cama")
                add("Bañarse")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_name -> addTask(0)
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addTask(posicion: Int): Boolean {
        tasks.add(posicion, "New name "+ (++counter))
        todoAdapter.notifyItemInserted(posicion)
        layoutManager.scrollToPosition(posicion)
        return true
    }
    private fun deleteTask(posicion: Int) {
        tasks.removeAt(posicion)
        todoAdapter.notifyItemRemoved(posicion)
    }


}