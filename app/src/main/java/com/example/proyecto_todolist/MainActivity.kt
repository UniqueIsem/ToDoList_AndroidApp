package com.example.proyecto_todolist
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var tasks: MutableList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var editTextTask: EditText
    private lateinit var btnAddTask: Button
    private var counter = 0
    private val calendar = Calendar.getInstance()
    private lateinit var radioHigh: RadioButton
    private lateinit var radioMedium: RadioButton
    private lateinit var radioLow: RadioButton
    private var selectedPriority: String? = null //spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showMessageInfo()

        tasks = getAllTasks()
        editTextTask = findViewById(R.id.editTextTask)
        btnAddTask = findViewById(R.id.btnAddTask)
        radioHigh = findViewById(R.id.radioHigh)
        radioMedium = findViewById(R.id.radioMedium)
        radioLow = findViewById(R.id.radioLow)
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
                Toast.makeText(this, "Ingrese un task e ingrese valor de importancia...", Toast.LENGTH_LONG).show()
            }
        }
        val prioritySpinner = findViewById<Spinner>(R.id.prioritySpinner)
        prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                selectedPriority = prioritySpinner.selectedItem.toString()
                filterTasksByPriority()
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
                selectedPriority = null
                filterTasksByPriority()
            }
        }
    }

    private fun filterTasksByPriority() {
        val filteredTasks = if (selectedPriority.isNullOrEmpty() || selectedPriority == "All") {
            // Si no se ha seleccionado ninguna prioridad o se selecciona "All", muestra todas las tareas
            tasks
        } else {
            // Filtra las tareas por la prioridad seleccionada
            tasks.filter { task -> task.contains("$selectedPriority") }
        }
        // Actualiza el adaptador con las tareas filtradas
        todoAdapter.updateTasks(filteredTasks)
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
        // Configurar DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                // Configurar TimePickerDialog
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _: TimePicker, hourOfDay: Int, minute: Int ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        // Formatear la fecha y hora seleccionadas
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val selectedDateTime = dateFormat.format(calendar.time)


                        // Agregar la tarea al RecyclerView con la fecha y hora seleccionadas
                        val newTask = editTextTask.text.toString()
                        val message = newTask
                        val hora = calendar.get(Calendar.HOUR_OF_DAY)
                        val minutos = calendar.get(Calendar.MINUTE)
                        setAlarm(message, hora, minutos)
                        addTask(0, newTask, selectedDateTime)
                        todoAdapter.notifyDataSetChanged()  // Asegúrate de tener una referencia a tu adaptador y descomentar esta línea si es necesario
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Mostrar DatePickerDialog
        datePickerDialog.show()
    }

    private fun setAlarm(mensaje: String, hora: Int, minutos: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            .putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
            .putExtra(AlarmClock.EXTRA_HOUR, hora)
            .putExtra(AlarmClock.EXTRA_MINUTES, minutos)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        Toast.makeText(this, "Alarma configurada", Toast.LENGTH_SHORT).show()
    }

    private fun addTask(posicion: Int, newTask: String, dateTime: String): Boolean {
        if (newTask.isNotEmpty() && (radioHigh.isChecked || radioMedium.isChecked || radioLow.isChecked)) {
            val importance = when {
                radioHigh.isChecked -> "High"
                radioMedium.isChecked -> "Medium"
                radioLow.isChecked -> "Low"
                else -> "Unknown"
            }

            tasks.add(posicion, newTask + "       " + "($importance)" + "      " + dateTime)
            todoAdapter.notifyItemInserted(posicion)
            layoutManager.scrollToPosition(posicion)
            editTextTask.text.clear()

            filterTasksByPriority()
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

    data class Task(val name: String, val dateTime: String, val importance: String)

}