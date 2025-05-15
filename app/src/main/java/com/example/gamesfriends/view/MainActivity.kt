package com.example.gamesfriends.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamesfriends.R
import com.example.gamesfriends.model.DataBaseHelper
import com.example.gamesfriends.viewModel.Gestor

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        dbHelper = DataBaseHelper(this)

        // intento de colarlo toodito en un json
//        dbHelper.deleteBBDD()
//        dbHelper.datosMninimos()

        // comprobacion de registro previo
        val estadoRegristro = Gestor(this)
        if (estadoRegristro.comprobarEstadoRegistro()) {
            Toast.makeText(this, "Sesión ya iniciada", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Cuerpo_app::class.java))
        }

        //
        val txtCorreo = findViewById<EditText>(R.id.etxt_correo_inicioSesion)
        val txtContrasenia = findViewById<EditText>(R.id.etxt_contrasenia_inicioSesion)
        val b_entrar = findViewById<Button>(R.id.b_entrar_inicioSesion)
        val txtContraseniaOlvidada = findViewById<TextView>(R.id.txt_contrasenia_olvidada)
        val tXTRegistrarse = findViewById<TextView>(R.id.txt_registrarse_inicioSesion)


        b_entrar.setOnClickListener {

            if (txtCorreo.text.isNullOrEmpty() || txtContrasenia.text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Se necesita correo y contraseña para iniciar sesión",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val correoTexto = txtCorreo.text.toString()
                val correoRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")

                if (!correoRegex.matches(correoTexto)) {
                    Toast.makeText(this, "El correo no tiene un formato válido", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else {
                    var idUsuario = dbHelper.incioSesion(
                        txtCorreo.text.toString(),
                        txtContrasenia.text.toString()
                    )
                    if (idUsuario > 0) {
                        val intent = Intent(this, Cuerpo_app::class.java)
                        intent.putExtra("ID_USUARIO", idUsuario)
                        startActivity(intent)
                        Toast.makeText(
                            this,
                            "Bienvenide" + idUsuario.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        estadoRegristro.estaRegristrado(true)
                        estadoRegristro.guardarIdRegistro(idUsuario)

                    } else {
                        Toast.makeText(
                            this,
                            "El correo o contraseña son erróneos :)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }


        tXTRegistrarse.setOnClickListener {
            val intent = Intent(this, Regristro::class.java)
            startActivity(intent)
        }

    }

    ////////////////////////////////////CIERRE AL DAR ATRAS/////////////////////

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }
}