package com.example.proyecto_todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_todolist.R
import com.example.proyecto_todolist.TodoItem

class TodoAdapter(var tasks: List<String>, var layout: Int, var itemListener: OnItemClickListener) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        val vh = TodoViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(tasks.get(position), itemListener)

    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(task: String?, itemListener: OnItemClickListener){
            textView.text = task
            itemView.setOnClickListener {
                itemListener.onItemclick(task, absoluteAdapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemclick(task: String?, position: Int)
    }
}