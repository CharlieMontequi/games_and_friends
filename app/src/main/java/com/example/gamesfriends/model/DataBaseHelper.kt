package com.example.gamesfriends.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class DataBaseHelper( var contexto:Context) :
    SQLiteOpenHelper(contexto, DATABASE, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE = "Juegos De Mesa"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USUARIOS = "Usuarios"
        private const val KEY_ID_USUARIO = "UsuarioID"
        private const val KEY_NOMBRE_USUARIO ="Nombre Usuario"// el nombre sea el que fuere mas el id
        private const val KEY_CORREO ="Correo"
        private const val KEY_CONTRASENIA="Contraseña"

        /*
        la tabla amigos mostrará las relaciones de amistad entre los jugadores
        eso solo funcionara en modo online ya que en local no habra sincronizacion
         */
        private const val TABLE_AMIGOS = "Amigos"


        /*
        faltaria determinar como van a ser los apuntes personales de los juegos
         */
        private const val TABLE_JUEGOS_EN_PROPIEDAD = "Juegos En Propiedad"
        private const val KEY_JUEGO_PROPIEDAD_ID ="JuedoPropiedadID"
        private const val KEY_FK_PROPIETARIO ="Propietario_ID"// id del juegador que lo tiene
        private const val KEY_FK_JUEGO_TIENE="JuegoTieneID"// id del juego que tiene
        private const val KEY_FECHA_ULTIMA_VEZ_JUEGADO ="Fecha Ultima Vez Jugado"
        private const val KEY_ESTA_AMORTIZADO ="Esta Amortizado"
        private const val KEY_VECES_PARA_AMORTIZAR="Veces Para Amortizarlo"

        /*
        especificar si va a tener duracion minima y maxima
        falta poder meter las fotos
         */
        private const val TABLE_JUEGOS ="Juegos"
        private const val KEY_ID_JUEGO ="JuegoID"
        private const val KEY_NOMBRE_JUEGO ="Nombre Juego"
        private const val KEY_NUMERO_JUGADORES_MINIMO="Número Jugadores Mínimo"
        private const val KEY_NUMERO_JUGADORES_MAXIMO="Número Jugadores MINIMO"
        private const val KEY_DURACION="Duración"
        private const val KEY_FK_CATEGORIA ="Categoria Juego"

        /*
        la tabla de tipos se va a encargar solo de recoger los tipos de juegos
        se ha añadido como tabla para que se filtre por id y sacarlos con una consulta
         */
        private const val TABLE_CATEGORIAS_DE_JUEGOS ="Categorias De Juegos"
        private const val KEY_ID_CATEGORIA_JUEGO="CategoriaID"
        private const val KEY_NOMBRE_CATEGORIA= "Nombre Categoria"

        /*
        la tabal historial sea una tabla desconectada dle resto que contendrá únicamente:
        - id propio
        - id usuario
        - id juegos usados
        - fecha
        eso es para evitar una base de datos que haga un bucle
         */
        private const val TABLE_HISTORIAL = "Historial"
        private const val KEY_ID_HISTORIAL ="HistorialID"
        private const val KEY_NOMBRE_PARTIDA ="Nombre Partida"
        private const val KEY_FECHA_PARTIDA ="Fecha Partida"
        private const val KEY_NUMERO_JUGADORES="Número Jugadores"// cuando exista el online esto sera una fk
        private const val KEY_JUEGOS_USADOS="Juegos Jugaods"// array de los juegos a los que se ha jugado


    }

    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}
