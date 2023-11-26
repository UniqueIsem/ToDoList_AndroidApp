package com.example.proyecto_todolist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    var tasks: List<String>,
    var layout: Int,
    var itemListener: OnItemClickListener
    ) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val selectedTasks = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(tasks.get(position), itemListener)
    }

    fun getSelectedItems(): Set<String> {
        return selectedTasks.toSet() //Cambio de string a set y to string a toSet
    }


    fun toggleSelection(task: String) {
        if (selectedTasks.contains(task)) {
            selectedTasks.remove(task)
        } else {
            selectedTasks.add(task)
        }
    }

    class TodoViewHolder(
        itemView: View,
        private val adapter: TodoAdapter
        ) : RecyclerView.ViewHolder(itemView) {

        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(task: String, itemListener: OnItemClickListener){
            checkBox.isChecked = adapter.selectedTasks.contains(task)
            textView.text = task

            itemView.setOnClickListener {
                itemListener.onItemclick(task, absoluteAdapterPosition)
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                Log.i("fdsaf",task)
                adapter.toggleSelection(task)
            }
        }

        fun getSelectedTasks(): List<String> {
            val selectedTasksList = mutableListOf<String>()
            for (task in adapter.selectedTasks) {
                if (adapter.selectedTasks.contains(task)) {
                    selectedTasksList.add(task)
                }
            }
            return selectedTasksList
        }


    }

    interface OnItemClickListener {
        fun onItemclick(task: String?, position: Int)
    }
}