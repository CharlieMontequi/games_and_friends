package com.example.gamesfriends.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

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
        private const val KEY_PRECIO_COMPRA = "Precio compra"
        private const val KEY_VECES_JUGADO = "Veces jugeado"
        private const val KEY_ANOTACION_PERSONAL = "Anotación personal"

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


        // TABLA TODAS LAS MECANICAS
        /*
        la tabla de tipos se va a encargar solo de recoger los tipos de juegos
        se ha añadido como tabla para que se filtre por id y sacarlos con una consulta
         */
        private const val TABLE_MECANICAS = "Mecánicas De Juegos"
        private const val KEY_ID_MECANICA = "MecánicasID"
        private const val KEY_NOMBRE_MECANICA = "Nombre Mecánicas"

        // TABLA JUEGOS CON CATEGORIAS
        private const val TABLE_MECANICAS_EN_JUEGO = "Mecánicas en juego"
        private const val KEY_ID_MECANICASENJUEGO = "Mecánicas_juegoID"
        private const val KEY_FK_JUEGO_MECANICASENJUEGO = "FK Juego ID"
        private const val KEY_FK_MECANICAS_MECANICASENJUEGO = "FK Mecánica ID"

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
        private const val KEY_FK_USUARIO_MONTA_PARTIDA = "FK Usuario Organizador"

        //TABLA DE JUEGOS LLEVADOS A LA PARTIDA
        /*
        Al ser los juegos usando un array de los mismos se define en otra tabla
         */
        val TABLE_JUEGOS_USADOS = "Tabla juegos llevados a la partida"
        val KEY_ID_JUEGO_USADO = "id juegos llevados"
        val KEY_FK_JUEGO_ID = "id del juego llevado"
        val KEY_FK_HISTORIAL = " id del historial"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaUsuarios =
            ("CREATE TABLE IF NOT EXISTS $TABLE_USUARIOS (" +
                    "$KEY_ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$KEY_NOMBRE_USUARIO TEXT NOT NULL, " +
                    "$KEY_CORREO TEXT NOT NULL, " +
                    "$KEY_CONTRASENIA TEXT NOT NULL)")

        val crearTablaJuegos = ("CREATE TABLE IF NOT EXISTS $TABLE_JUEGOS(" +
                "$KEY_ID_JUEGO INTEGENER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_NOMBRE_JUEGO TEXT NOT NULL, " +
                "$KEY_NUMERO_JUGADORES_MINIMO INTEGER, " +
                "$KEY_NUMERO_JUGADORES_MAXIMO INTEGER, " +
                "$KEY_DURACION INTEGER)")

        val crearTablaMecanicas = ("CREATE TABLE IF NOT EXISTS $TABLE_MECANICAS(" +
                "$KEY_ID_MECANICA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_NOMBRE_MECANICA TEXT NOT NULL)")

        val crearTablaJuegosPropiedad = ("CREATE TABLE IF NOT EXISTS $TABLE_JUEGOS_EN_PROPIEDAD(" +
                "$KEY_JUEGO_PROPIEDAD_ID INTEGER PRIMARY KEY, " +
                "$KEY_FK_PROPIETARIO INTEGER," +
                "$KEY_FK_JUEGO_TIENE INTEGER, " +
                "$KEY_FECHA_ULTIMA_VEZ_JUEGADO TEXT," +
                "$KEY_VECES_JUGADO INTEGER," +
                "$KEY_ANOTACION_PERSONAL TEXT," +
                "$KEY_PRECIO_COMPRA DOUBLE NOT NULL," +
                "FOREIGN KEY($KEY_FK_PROPIETARIO) REFERENCES $TABLE_USUARIOS($KEY_ID_USUARIO))," +
                "FOREIGN KEY ($KEY_FK_JUEGO_TIENE) REFERENCES $TABLE_JUEGOS($KEY_ID_JUEGO))")

        val crearTablaMecanicasEnJuego = ("CREATE TABLE IF NOT EXISTS $TABLE_MECANICAS_EN_JUEGO(" +
                "$KEY_ID_MECANICASENJUEGO INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_FK_JUEGO_MECANICASENJUEGO INTEGER," +
                "$KEY_FK_MECANICAS_MECANICASENJUEGO INTEGER," +
                "FOREING KEY ($KEY_FK_JUEGO_MECANICASENJUEGO) REFERENCES $TABLE_JUEGOS($KEY_ID_JUEGO))," +
                "FOREING KEY ($KEY_FK_MECANICAS_MECANICASENJUEGO) REFERENCES $TABLE_MECANICAS($KEY_ID_MECANICA))")

        val crearTablaHistorial = ("CREATE TABLE IF NOT EXISTS $TABLE_HISTORIAL(" +
                "$KEY_ID_HISTORIAL INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_NOMBRE_PARTIDA TEXT NOT NULL," +
                "$KEY_FECHA_PARTIDA TEXT NOT NULL," +
                "$KEY_NUMERO_JUGADORES INTEGER +" +
                "$KEY_FK_USUARIO_MONTA_PARTIDA INTEGER," +
                "FOREIGN KEY ($KEY_FK_USUARIO_MONTA_PARTIDA) REFERENCES $TABLE_USUARIOS($KEY_ID_USUARIO))")

        val crearTablaJuegosUsados = ("CREATE TABLE IF NOT EXISTS $TABLE_JUEGOS_USADOS (" +
                "$KEY_ID_JUEGO_USADO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_FK_HISTORIAL INTEGER NOT NULL, " +
                "$KEY_FK_JUEGO_ID INTEGER NOT NULL, " +
                "FOREIGN KEY ($KEY_ID_HISTORIAL) REFERENCES $TABLE_HISTORIAL($KEY_ID_HISTORIAL))")

        // val creaTablaAmistades=""

        if (db != null) {
            db.execSQL(crearTablaUsuarios)
            Log.d("TABLAS", "tabla usuarios creada con exito")

            db.execSQL(crearTablaJuegos)
            Log.d("TABLAS", "tabla juegos creada con exito")

            db.execSQL(crearTablaMecanicas)
            Log.d("TABLAS", "tabla mecanicas creada con exito")

            db.execSQL(crearTablaJuegosPropiedad)
            Log.d("TABLAS", "tabla juegos en propiedad creada con exito")

            db.execSQL(crearTablaMecanicasEnJuego)
            Log.d("TABLAS", "tabla mecanicas en juego creada con exito")

            db.execSQL(crearTablaHistorial)
            Log.d("TABLAS", "tabla historial creada con exito")

            db.execSQL(crearTablaJuegosUsados)
            //  db.execSQL(creaTablaAmistades)
            //  Log.d("TABLAS","tabla amigos creada con exito")

        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_JUEGOS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_MECANICAS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_JUEGOS_EN_PROPIEDAD")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_MECANICAS_EN_JUEGO")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORIAL")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_JUEGOS_USADOS")
            //db.execSQL("DROP TABLE IF EXISTS $TABLE_AMIGOS")

            onCreate(db)
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////
    //////////////////////////CREAR NUEVOS ELEMENTOS///////////////////////////////////

    fun crearUsuario(){

    }
    fun crearJuego(){

    }

    fun crecarMecancia(){}

    fun crearJuegoEnPropiedad(){}

    fun crearMecanciaEnJuego(){}

    fun crearHistorial(){}

    fun crearJuegoEnUso(){}


    ///////////////////////////FECHAS GESTOR///////////////////////////////////////////
    // conversor de fechas
    fun deFechaATexto(fecha: Date): String {
        val formatoFecha = SimpleDateFormat("dd-MM-yyyy")
        return formatoFecha.format(fecha)
    }

    fun deTextoAFecha(textoFechado: String): LocalDate? {
        return try {
            val formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            LocalDate.parse(textoFechado, formatoFecha) // para sacar la fecha sin la hora
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
