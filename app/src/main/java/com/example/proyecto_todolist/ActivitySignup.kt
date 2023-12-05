package com.example.proyecto_todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import Usuario

class ActivitySignup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val usuarios: MutableList<Usuario> = mutableListOf()

        var newUser: EditText = findViewById(R.id.usuario)
        var psw: EditText = findViewById(R.id.password)
        var btnSignUp: Button = findViewById(R.id.btnSignUp)
        var toggle: ToggleButton = findViewById(R.id.toggleBtn)

        btnSignUp.setOnClickListener {
            val username = newUser.text.toString()
            val password = psw.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val usuario = Usuario(username, password)
                usuarios.add(usuario)

                Toast.makeText(this, "Usuario " + username + " creado con exito", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ActivityHome::class.java)
                intent.putParcelableArrayListExtra("listaUsuarios", ArrayList(usuarios))
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Ingrese un usuario y contraseña", Toast.LENGTH_LONG).show()
            }
        }
        toggle.setOnClickListener { view ->
            val isChecked = toggle.isChecked
            if (isChecked){
                psw.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                psw.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

}