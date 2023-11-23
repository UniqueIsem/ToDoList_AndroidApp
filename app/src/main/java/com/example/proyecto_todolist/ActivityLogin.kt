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

class ActivityLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var user: EditText = findViewById(R.id.usuario)
        var psw: EditText = findViewById(R.id.password)
        var btnSave: Button = findViewById(R.id.btnSave)
        var toggle: ToggleButton = findViewById(R.id.toggleBtn)

        btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                var strUser = user.text.toString()
                var strPsw = psw.text.toString()
                user.setText("")
                psw.setText("")
                login(strUser, strPsw)
            }
        })
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