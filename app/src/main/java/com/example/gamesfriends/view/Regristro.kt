package com.example.gamesfriends.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.model.datos.Usuario

class Regristro : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_nuevo_usuario)

        dbHelper = DataBaseHelper(this)

        val txtNombre = findViewById<EditText>(R.id.etxt_nombre_registro)
        val txtCorreo = findViewById<EditText>(R.id.etxt_correo_registro)
        val txtContrasenia = findViewById<EditText>(R.id.etxt_contrasenia_registro)
        val b_registrarse = findViewById<Button>(R.id.b_registro_registro)


        b_registrarse.setOnClickListener {
            if (txtCorreo.text.isNullOrEmpty() || txtContrasenia.text.isNullOrEmpty() || txtNombre.text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Se necesitaa nombre, correo y contraseña :3",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val idUsuario = dbHelper.crearUsuario(
                    Usuario(
                        null,
                        nombre_usuario = txtNombre.text.toString(),
                        correo_usuario = txtCorreo.text.toString(),
                        contrasenia_usuario = txtContrasenia.text.toString()
                    )
                )
                Toast.makeText(
                    this,
                    "Usuario registrado con exito :)" + idUsuario.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                if (idUsuario > 0) {
                    val intent = Intent(this, Cuerpo_app::class.java)
                    intent.putExtra("ID_USUARIO", idUsuario)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "INTIMISIMI",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

        }

    }
}