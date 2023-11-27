package com.example.proyecto_todolist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import android.app.DatePickerDialog
import java.util.Calendar
import android.app.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.Locale

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

        showMessageInfo()

        tasks = getAllTasks()
        editTextTask = findViewById(R.id.editTextTask)
        btnAddTask = findViewById(R.id.btnAddTask)
        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)
        todoAdapter =
            TodoAdapter(tasks, R.layout.item_todo,object : TodoAdapter.OnItemClickListener {
                override fun onItemclick(name: String?, position: Int) {
                    deleteTask(position)
                }
            })
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setAdapter(todoAdapter)

        btnAddTask.setOnClickListener {
            val editText = editTextTask.text.toString()
            if (editText.isNotEmpty()) {
                showDateTimePicker()
            } else {
                Toast.makeText(this, "Ingrese un task...", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun showMessageInfo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("INFORMACIÓN")
        builder.setMessage("Podrás agregar tareas sencillas escribiéndolas en la barra de texto y presionando el botón de agregar en la barra de menú. Si deseas agregar tareas con un tiempo o fecha específicos, selecciona el botón \"Add Big Task\".")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun getAllTasks(): MutableList<String> {
        return object : ArrayList<String>() {
            init {
                add("Este es un ejemplo de un task...")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addTask -> {
                val newTask = editTextTask.text.toString()
                val vacio = ""
                //Agregamos un task sin fecha ni hora
                addTask(0, newTask, vacio)
                true
            }
            R.id.info -> {
                showMessageInfo()
                true
            }
            R.id.removeSelected -> {
                deleteTasksSelected()
                true
            }
            R.id.removeAll -> {
                deleteAll()
                true
            }
            R.id.exit -> {
                exit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        // Configuracion DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)

                // Configuracion TimePickerDialog
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        // Formatear la fecha y hora seleccionadas
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val selectedDateTime = dateFormat.format(calendar.time)

                        // Agrega la tarea al RecyclerView con la fecha y hora seleccionadas
                        val newTask = editTextTask.text.toString()
                        val time = selectedDateTime.toString()
                        addTask(0, newTask, time)
                        todoAdapter.notifyDataSetChanged()
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )

                // Mostrar TimePickerDialog
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Mostrar DatePickerDialog
        datePickerDialog.show()
    }

    private fun addTask(posicion: Int, newTask: String, dateTime: String): Boolean {
        if (newTask.isNotEmpty()) {
            tasks.add(posicion, newTask + "       " + dateTime)
            todoAdapter.notifyItemInserted(posicion)
            layoutManager.scrollToPosition(posicion)
            editTextTask.text.clear()
        } else {
            Toast.makeText(this, "Ingrese un task...", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun deleteTask(posicion: Int) {
        tasks.removeAt(posicion)
        todoAdapter.notifyItemRemoved(posicion)
    }

    private fun deleteTasksSelected() {
        val selectedTasks = todoAdapter.getSelectedItems()
        tasks.removeAll(selectedTasks)
        todoAdapter.notifyDataSetChanged()
    }

    private fun deleteAll() {
        tasks.clear()
        todoAdapter.notifyDataSetChanged()
    }

    private fun exit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("EXIT")
        builder.setMessage("¿Seguro que quieres salir de la sesión?")
        builder.setPositiveButton("Sí") { dialog, which ->
            finish()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

data class Task(val name: String, val dateTime: String)

}