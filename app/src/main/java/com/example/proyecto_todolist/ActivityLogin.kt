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

class ActivityLogin : AppCompatActivity() {

    private val usuarios: MutableList<Usuario> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var user: EditText = findViewById(R.id.usuario)
        var psw: EditText = findViewById(R.id.password)
        var btnLogin: Button = findViewById(R.id.btnLogin)
        var toggle: ToggleButton = findViewById(R.id.toggleBtn)
        //usuarios = intent.getParcelableArrayListExtra("listaUsuarios") ?: mutableListOf()

        btnLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                var strUser = user.text.toString()
                var strPsw = psw.text.toString()
                user.setText("")
                psw.setText("")
                login(strUser, strPsw)
            }
        })
        /*btnLogin.setOnClickListener {
            val username = user.text.toString()
            val password = psw.text.toString()

            val usuarioValido = usuarios.find { it.username == username && it.password == password }

            if (usuarioValido != null) {
                Toast.makeText(this, "Bienvendido", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas o usuario inexistente", Toast.LENGTH_LONG).show()
            }
        }*/
        toggle.setOnClickListener { view ->
            val isChecked = toggle.isChecked
            if (isChecked){
                psw.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                psw.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    fun login (strUser: String, strPsw: String) {
        if (strUser == "Isaac" && strPsw == "123") {
            Toast.makeText(this, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Credenciales incorrectas, intentelo de nuevo", Toast.LENGTH_LONG).show()
        }
    }

}