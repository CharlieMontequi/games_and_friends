package com.example.gamesfriends.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class DataBaseHelper(var contexto: Context) :
    SQLiteOpenHelper(contexto, DATABASE, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE = "Juegos De Mesa"
        private const val DATABASE_VERSION = 1

        // TABLA USUARIOS
        private const val TABLE_USUARIOS = "Usuarios"
        private const val KEY_ID_USUARIO = "UsuarioID"
        private const val KEY_NOMBRE_USUARIO =
            "Nombre Usuario"// el nombre sea el que fuere mas el id
        private const val KEY_CORREO = "Correo"
        private const val KEY_CONTRASENIA = "Contraseña"

        // TABLA AMIGOS
        /*
        la tabla amigos mostrará las relaciones de amistad entre los jugadores
        eso solo funcionara en modo online ya que en local no habra sincronizacion
         */
        private const val TABLE_AMIGOS = "Amigos"

        // TABLA JUEGOS EN PROPIEDEAS/COLECCION
        /*
        faltaria determinar como van a ser los apuntes personales de los juegos
         */
        private const val TABLE_JUEGOS_EN_PROPIEDAD = "Juegos En Propiedad"
        private const val KEY_JUEGO_PROPIEDAD_ID = "JuedoPropiedadID"
        private const val KEY_FK_PROPIETARIO = "FK Propietario_ID"// id del juegador que lo tiene
        private const val KEY_FK_JUEGO_TIENE = "FK JuegoTieneID"// id del juego que tiene
        private const val KEY_FECHA_ULTIMA_VEZ_JUEGADO = "Fecha Ultima Vez Jugado"
        private const val KEY_ESTA_AMORTIZADO = "Esta Amortizado"
        private const val KEY_VECES_PARA_AMORTIZAR = "Veces Para Amortizarlo"

        // TABLA TODOS LOS JUEGOS
        /*
        especificar si va a tener duracion minima y maxima
        falta poder meter las fotos
         */
        private const val TABLE_JUEGOS = "Juegos"
        private const val KEY_ID_JUEGO = "JuegoID"
        private const val KEY_NOMBRE_JUEGO = "Nombre Juego"
        private const val KEY_NUMERO_JUGADORES_MINIMO = "Número Jugadores Mínimo"
        private const val KEY_NUMERO_JUGADORES_MAXIMO = "Número Jugadores MINIMO"
        private const val KEY_DURACION = "Duración"

        // TABLA TODAS LAS CATEGORIAS CATEGORIAS
        /*
        la tabla de tipos se va a encargar solo de recoger los tipos de juegos
        se ha añadido como tabla para que se filtre por id y sacarlos con una consulta
         */
        private const val TABLE_CATEGORIAS_DE_JUEGOS = "Categorias De Juegos"
        private const val KEY_ID_CATEGORIA_JUEGO = "CategoriaID"
        private const val KEY_NOMBRE_CATEGORIA = "Nombre Categoria"

        // TABLA JUEGOS CON CATEGORIAS
        private const val TABLE_JUEGOS_TIENEN_CATEGORIAS = "Juegos Tienen Categorias"
        private const val KEY_ID_TIENEN_CATEGORIAS = "TienenCategoriasID"
        private const val KEY_FK_JUEGO_CATEGORIA = "FK Juego ID"
        private const val KEY_FK_CATEGORIA_EN_JUEGOç = "FK Categoria ID"

        // TABLA HISTORIAL DE PARTIDAS- independiente
        /*
        la tabal historial sea una tabla desconectada dle resto que contendrá únicamente:
        - id propio
        - id usuario
        - id juegos usados
        - fecha
        eso es para evitar una base de datos que haga un bucle
         */
        private const val TABLE_HISTORIAL = "Historial"
        private const val KEY_ID_HISTORIAL = "HistorialID"
        private const val KEY_NOMBRE_PARTIDA = "Nombre Partida"
        private const val KEY_FECHA_PARTIDA = "Fecha Partida"
        private const val KEY_NUMERO_JUGADORES =
            "Número Jugadores"// cuando exista el online esto sera una fk
        private const val KEY_JUEGOS_USADOS =
            "Juegos Jugaods"// array de los juegos a los que se ha jugado
        private const val KEY_FK_USUARIO_MONTA_PARTIDA = "FK Usuario Organizador"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaUsuarios =
            ("CREATE TABLE IF NOT EXISTS $TABLE_USUARIOS (" +
                    "$KEY_ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$KEY_NOMBRE_USUARIO TEXT NOT NULL, " +
                    "$KEY_CORREO TEXT NOT NULL, " +
                    "$KEY_CONTRASENIA TEXT NOT NULL)")

        val crearTablaJuegos=("CREATE TABLE IF NOT EXISTS $TABLE_JUEGOS(" +
                "$KEY_ID_JUEGO INTEGENER PRIMARY KEY AUTOINCREMENT, "+
                "$KEY_NOMBRE_JUEGO TEXT NOT NULL, " +
                "$KEY_NUMERO_JUGADORES_MINIMO INTEGER, " +
                "$KEY_NUMERO_JUGADORES_MAXIMO INTEGER, " +
                "$KEY_DURACION INTEGER)")

        val crearTablaCategorias=("CREATE TABLE IF NOT EXISTS $TABLE_CATEGORIAS_DE_JUEGOS(" +
                "$KEY_ID_CATEGORIA_JUEGO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_NOMBRE_CATEGORIA TEXT NOT NULL)")

        val crearTablaJuegosPropiedad=("CREATE TABLE IF NOT EXISTS $TABLE_JUEGOS_EN_PROPIEDAD(" +
                "$KEY_JUEGO_PROPIEDAD_ID INTEGER PRIMARY KEY, " +
                "$KEY_FK_PROPIETARIO INTEGER," +
                "$KEY_FK_JUEGO_TIENE INTEGER, " +
                "$KEY_FECHA_ULTIMA_VEZ_JUEGADO DATE," +
                "$KEY_ESTA_AMORTIZADO BOOLEAN," +
                "$KEY_VECES_PARA_AMORTIZAR INTEGER")
        val crearTablaJuegosConCategoria=""

        val crearTablaHistorial=""

        val creaTablaAmistades=""

        if(db!=null){
            db.execSQL(crearTablaUsuarios)
            db.execSQL(crearTablaJuegos)
            db.execSQL(crearTablaCategorias)
            db.execSQL(crearTablaJuegosPropiedad)
            db.execSQL(crearTablaJuegosConCategoria)
            db.execSQL(crearTablaHistorial)
            db.execSQL(creaTablaAmistades)


        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}
