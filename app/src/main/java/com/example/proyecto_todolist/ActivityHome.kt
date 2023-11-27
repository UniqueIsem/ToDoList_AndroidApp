package com.example.proyecto_todolist

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class ActivityHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun onCardClick(view: View) {
        when (view.id) {
            R.id.cardSignup -> {
                // Handle click for option 1
                startActivity(Intent(this, ActivityLogin::class.java))
            }
            R.id.cardLogin -> {
                // Handle click for option 2
                startActivity(Intent(this, ActivityLogin::class.java))
            }
            R.id.cardInfo -> {
                showMessageInfo()
            }
            R.id.cardExit -> {
                exit()
            }
        }
    }

    private fun exit() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("EXIT")
        builder.setMessage("¿Seguro que quieres salir de la aplicacion?")
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

    private fun showMessageInfo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("INFORMACIÓN")
        builder.setMessage("Al iniciar sesion en nuestra app, podrás crear una lista organizada de las tareas diarias o futuras sin la necesidad de tener que recordarlas.\n" +
                "La aplicacion se encuentra en fase beta, por lo cual las opciones están reducidas")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}