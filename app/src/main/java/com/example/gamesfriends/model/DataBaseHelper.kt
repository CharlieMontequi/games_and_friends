package com.example.gamesfriends.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.gamesfriends.model.datos.Amigo
import com.example.gamesfriends.model.datos.Historial
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.model.datos.Coleccion
import com.example.gamesfriends.model.datos.JuegoUsado
import com.example.gamesfriends.model.datos.Mecanica
import com.example.gamesfriends.model.datos.MecanicaEnJuego
import com.example.gamesfriends.model.datos.Usuario
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class DataBaseHelper(var contexto: Context) :
    SQLiteOpenHelper(contexto, DATABASE, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE = "Juegos_De_Mesa"
        private const val DATABASE_VERSION = 4

        // TABLA USUARIOS
        private const val TABLE_USUARIOS = "Usuarios"
        private const val KEY_ID_USUARIO = "UsuarioID"
        private const val KEY_NOMBRE_USUARIO = "Nombre_Usuario"
        private const val KEY_CORREO = "Correo"
        private const val KEY_CONTRASENIA = "Contraseña"

        // TABLA AMIGOS
        /*
        la tabla amigos mostrará las relaciones de amistad entre los jugadores
        eso solo funcionara en modo online ya que en local no habra sincronizacion
         */
        private const val TABLE_AMIGOS = "Amigos"
        private const val KEY_ID_AMIGOS = "ID_amigos"
        private const val KEY_FK_AMIGO1 = "FK_amigo1"
        private const val KEY_FK_AMIGO2 = "FK_amigo2"

        // TABLA JUEGOS EN PROPIEDEAS/COLECCION
        /*
        faltaria determinar como van a ser los apuntes personales de los juegos
         */
        private const val TABLE_COLECCION = "Juegos_En_Propiedad"
        private const val KEY_ID_COLECCION = "JuedoPropiedadID"
        private const val KEY_FK_USUARIO_COLECCIONA_JUEGO = "FK_Propietario_ID"
        private const val KEY_FK_JUEGO_COLECCIONADO = "FK_JuegoTieneID"
        private const val KEY_FECHA_ULTIMA_VEZ_JUEGADO = "Fecha_Ultima_Vez_Jugado"
        private const val KEY_PRECIO_COMPRA = "Precio_compra"
        private const val KEY_VECES_JUGADO = "Veces_jugado"
        private const val KEY_ANOTACION_PERSONAL = "Anotación_personal"

        // TABLA TODOS LOS JUEGOS
        /*
        especificar si va a tener duracion minima y maxima
        falta poder meter las fotos
         */
        private const val TABLE_JUEGOS = "Juegos"
        private const val KEY_ID_JUEGO = "JuegoID"
        private const val KEY_NOMBRE_JUEGO = "Nombre_Juego"
        private const val KEY_DESCIPRCION_JUEGO = "Descripción"
        private const val KEY_NUMERO_JUGADORES_MINIMO = "Mínimo"
        private const val KEY_NUMERO_JUGADORES_MAXIMO = "Máximo"
        private const val KEY_DURACION = "Duración"


        // TABLA TODAS LAS MECANICAS
        /*
        la tabla de tipos se va a encargar solo de recoger los tipos de juegos
        se ha añadido como tabla para que se filtre por id y sacarlos con una consulta
         */
        private const val TABLE_MECANICAS = "Mecánicas_De_Juegos"
        private const val KEY_ID_MECANICA = "MecánicasID"
        private const val KEY_NOMBRE_MECANICA = "Nombre_Mecánicas"

        // TABLA JUEGOS CON CATEGORIAS
        private const val TABLE_MECANICAS_EN_JUEGO = "Mecánicas_en_juego"
        private const val KEY_ID_MECANICASENJUEGO = "Mecánicas_juegoID"
        private const val KEY_FK_JUEGO_MECANICASENJUEGO = "FK_Juego_ID"
        private const val KEY_FK_MECANICAS_MECANICASENJUEGO = "FK_Mecánica_ID"

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
        private const val KEY_NOMBRE_PARTIDA = "Nombre_Partida"
        private const val KEY_FECHA_PARTIDA = "Fecha_Partida"
        private const val KEY_NUMERO_PERSONAS = "Número_Jugadores"
        private const val KEY_FK_USUARIO_MONTA_PARTIDA = "FK_Usuario_Organizador"

        //TABLA DE JUEGOS LLEVADOS A LA PARTIDA
        /*
        Al ser los juegos usando un array de los mismos se define en otra tabla
         */
        val TABLE_JUEGOS_USADOS = "Tabla_Juegos_Usadps"
        val KEY_ID_JUEGO_USADO = "id_JuegoUsado"
        val KEY_FK_JUEGO_JUEGOUSADO = "id_juego_juegoUsado"
        val KEY_FK_HISTORIAL = " id_historial"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaUsuarios =
            ("CREATE TABLE IF NOT EXISTS $TABLE_USUARIOS (" +
                    "$KEY_ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$KEY_NOMBRE_USUARIO TEXT, " +
                    "$KEY_CORREO TEXT, " +
                    "$KEY_CONTRASENIA TEXT)")

        val crearTablaJuegos = ("CREATE TABLE IF NOT EXISTS $TABLE_JUEGOS(" +
                "$KEY_ID_JUEGO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_NOMBRE_JUEGO TEXT, " +
                "$KEY_DESCIPRCION_JUEGO TEXT," +
                "$KEY_NUMERO_JUGADORES_MINIMO INTEGER DEFAULT 1, " +
                "$KEY_NUMERO_JUGADORES_MAXIMO INTEGER, " +
                "$KEY_DURACION INTEGER)")

        val crearTablaMecanicas = ("CREATE TABLE IF NOT EXISTS $TABLE_MECANICAS(" +
                "$KEY_ID_MECANICA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_NOMBRE_MECANICA TEXT NOT NULL)")

        val crearTablaJuegosPropiedad = ("CREATE TABLE IF NOT EXISTS $TABLE_COLECCION(" +
                "$KEY_ID_COLECCION INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_FECHA_ULTIMA_VEZ_JUEGADO TEXT," +
                "$KEY_VECES_JUGADO INTEGER," +
                "$KEY_ANOTACION_PERSONAL TEXT," +
                "$KEY_PRECIO_COMPRA REAL NOT NULL," +
                "$KEY_FK_USUARIO_COLECCIONA_JUEGO INTEGER," +
                "$KEY_FK_JUEGO_COLECCIONADO INTEGER, " +
                "FOREIGN KEY($KEY_FK_USUARIO_COLECCIONA_JUEGO) REFERENCES $TABLE_USUARIOS($KEY_ID_USUARIO)," +
                "FOREIGN KEY ($KEY_FK_JUEGO_COLECCIONADO) REFERENCES $TABLE_JUEGOS($KEY_ID_JUEGO))")

        val crearTablaMecanicasEnJuego = ("CREATE TABLE IF NOT EXISTS $TABLE_MECANICAS_EN_JUEGO(" +
                "$KEY_ID_MECANICASENJUEGO INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_FK_JUEGO_MECANICASENJUEGO INTEGER," +
                "$KEY_FK_MECANICAS_MECANICASENJUEGO INTEGER," +
                "FOREIGN KEY ($KEY_FK_JUEGO_MECANICASENJUEGO) REFERENCES $TABLE_JUEGOS($KEY_ID_JUEGO), " +
                "FOREIGN KEY ($KEY_FK_MECANICAS_MECANICASENJUEGO) REFERENCES $TABLE_MECANICAS($KEY_ID_MECANICA))")

        val crearTablaHistorial = ("CREATE TABLE IF NOT EXISTS $TABLE_HISTORIAL(" +
                "$KEY_ID_HISTORIAL INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_NOMBRE_PARTIDA TEXT," +
                "$KEY_FECHA_PARTIDA TEXT," +
                "$KEY_NUMERO_PERSONAS INTEGER, " +
                "$KEY_FK_USUARIO_MONTA_PARTIDA INTEGER," +
                "FOREIGN KEY ($KEY_FK_USUARIO_MONTA_PARTIDA) REFERENCES $TABLE_USUARIOS($KEY_ID_USUARIO))")

        val crearTablaJuegosUsados = ("CREATE TABLE IF NOT EXISTS $TABLE_JUEGOS_USADOS (" +
                "$KEY_ID_JUEGO_USADO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_FK_HISTORIAL INTEGER, " +
                "$KEY_FK_JUEGO_JUEGOUSADO INTEGER, " +
                "FOREIGN KEY ($KEY_FK_HISTORIAL) REFERENCES $TABLE_HISTORIAL($KEY_ID_HISTORIAL), " +
                "FOREIGN KEY ($KEY_FK_JUEGO_JUEGOUSADO) REFERENCES $TABLE_JUEGOS($KEY_ID_JUEGO))")

        val crearTablaAmigos = ("CREATE TABLE IF NOT EXISTS $TABLE_AMIGOS (" +
                "$KEY_ID_AMIGOS INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_FK_AMIGO1 INTEGER," +
                "$KEY_FK_AMIGO2 INTEGER," +
                "FOREIGN KEY($KEY_FK_AMIGO1) REFERENCES $TABLE_USUARIOS($KEY_ID_USUARIO)," +
                "FOREIGN KEY($KEY_FK_AMIGO2) REFERENCES $TABLE_USUARIOS($KEY_ID_USUARIO))")

        // val creaTablaAmistades=""

        if (db != null) {
            db.execSQL(crearTablaUsuarios)
            Log.d("TABLAS", "tabla usuarios creada con exito")

            db.execSQL(crearTablaJuegos)
            Log.d("TABLAS", "tabla juegos creada con exito")

            db.execSQL(crearTablaMecanicas)
            Log.d("TABLAS", "tabla mecanicas creada con exito")

            db.execSQL(crearTablaHistorial)
            Log.d("TABLAS", "tabla historial creada con exito")

            db.execSQL(crearTablaJuegosPropiedad)
            Log.d("TABLAS", "tabla juegos en propiedad creada con exito")

            db.execSQL(crearTablaMecanicasEnJuego)
            Log.d("TABLAS", "tabla mecanicas en juego creada con exito")

            db.execSQL(crearTablaJuegosUsados)

            db.execSQL(crearTablaAmigos)
            Log.d("TABLAS", "tabla amigos creada con exito")

        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_JUEGOS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_MECANICAS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_COLECCION")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_MECANICAS_EN_JUEGO")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORIAL")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_JUEGOS_USADOS")
            //db.execSQL("DROP TABLE IF EXISTS $TABLE_AMIGOS")


        }
    }

    ///////////////////////////BORRAR BBDD/////////////////////////////////////////////
    fun deleteBBDD() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JUEGOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MECANICAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COLECCION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MECANICAS_EN_JUEGO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORIAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JUEGOS_USADOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AMIGOS")
        onCreate(db)
    }
    //////////////////////////CREAR NUEVOS ELEMENTOS///////////////////////////////////

    fun crearUsuario(usuario: Usuario): Int {

        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_NOMBRE_USUARIO, usuario.nombre_usuario)
        datos.put(KEY_CORREO, usuario.correo_usuario)
        datos.put(KEY_CONTRASENIA, usuario.contrasenia_usuario)
        val id = db.insert(TABLE_USUARIOS, null, datos)

        return if (id != 1L) {
            id.toInt()
        } else {
            -1
        }
    }

    fun crearJuego(juego: Juego): Int {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_NOMBRE_JUEGO, juego.nombreJuego)
        datos.put(KEY_DESCIPRCION_JUEGO, juego.descipcionJuegp)
        datos.put(KEY_DURACION, juego.duracionJuego)
        datos.put(KEY_NUMERO_JUGADORES_MINIMO, juego.minimoJugadoresJuego)
        datos.put(KEY_NUMERO_JUGADORES_MAXIMO, juego.maximoJugadoresJuego)

        val idJuego = db.insert(TABLE_JUEGOS, null, datos)
        return idJuego.toInt()
    }

    fun crearMecancia(mecanica: Mecanica) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(
            KEY_NOMBRE_MECANICA, mecanica.nombreMecanica
        )
        db.insert(TABLE_MECANICAS, null, datos)
    }

    fun crearJuegoEnPropiedad(juegoEnPropiedad: Coleccion) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(
            KEY_PRECIO_COMPRA, juegoEnPropiedad.precioCompra_coleccion
        )
        datos.put(
            KEY_FECHA_ULTIMA_VEZ_JUEGADO,
            juegoEnPropiedad.ultimaVezJugado_coleccion
        )
        datos.put(KEY_VECES_JUGADO, juegoEnPropiedad.vecesJugado_coleccion)
        datos.put(KEY_ANOTACION_PERSONAL, juegoEnPropiedad.anotacionPersonal_coleccion)
        datos.put(KEY_FK_USUARIO_COLECCIONA_JUEGO, juegoEnPropiedad.fk_usuario_tiene_coleccion)
        datos.put(KEY_FK_JUEGO_COLECCIONADO, juegoEnPropiedad.fk_juego_en_coleccion)

        db.insert(TABLE_COLECCION, null, datos)
    }

    fun crearMecanciaEnJuego(mecanicaEnJuego: MecanicaEnJuego) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_FK_MECANICAS_MECANICASENJUEGO, mecanicaEnJuego.fk_mecanica_mecanicaJuego)
        datos.put(KEY_FK_JUEGO_MECANICASENJUEGO, mecanicaEnJuego.fk_juego_mecanicaJuego)

        db.insert(TABLE_MECANICAS_EN_JUEGO, null, datos)
        db.close()
    }

    fun crearHistorial(historial: Historial): Int {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_NOMBRE_PARTIDA, historial.nombre_historial)
        datos.put(KEY_FECHA_PARTIDA, historial.fecha_historial)
        datos.put(KEY_NUMERO_PERSONAS, historial.numeroPersonas_historial)
        datos.put(KEY_FK_USUARIO_MONTA_PARTIDA, historial.usuario_historial)


        val id = db.insert(TABLE_HISTORIAL, null, datos)
        return if (id != 1L) {
            id.toInt()
        } else {
            -1
        }
    }

    fun crearJuegoEnUso(juegoUsado: JuegoUsado) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_FK_JUEGO_JUEGOUSADO, juegoUsado.fkJuegoJuegousado)
        datos.put(KEY_FK_HISTORIAL, juegoUsado.fkHistorialJuegousado)

        db.insert(TABLE_JUEGOS_USADOS, null, datos)
    }

    fun crearAmigos(amigo: Amigo) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_FK_AMIGO1, amigo.fk_amigo1)
        datos.put(KEY_FK_AMIGO2, amigo.fk_amigo2)

        db.insert(TABLE_AMIGOS, null, datos)
    }

    //////////////////////////BORRAR ELEMENTOS/////////////////////////////////////////

    fun borrarUsuario(idUsuario: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_USUARIOS, "$KEY_ID_USUARIO =?", arrayOf(idUsuario.toString()))
    }

    // ESTA COMO EL ORTO DE MAL ESTA CONSULTA
    fun borrarJuegoDeJuegosTiene(idJuegoTiene: Int, idUsuario: Int) {
        val db = this.writableDatabase
        db.delete(
            TABLE_COLECCION,
            "$KEY_FK_JUEGO_COLECCIONADO=? AND $KEY_FK_USUARIO_COLECCIONA_JUEGO=?",
            arrayOf(idJuegoTiene.toString(), idUsuario.toString())
        )
    }

    fun borrarHistorial(idHistorial: Int) {
        val db = this.writableDatabase

        // se borra tambien de la conjunta
        borrarJuegoUsado(idHistorial)

        db.delete(TABLE_HISTORIAL, "$KEY_ID_HISTORIAL =?", arrayOf(idHistorial.toString()))

    }

    fun borrarJuegoUsado(fkHistorial: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_JUEGOS_USADOS, "$KEY_FK_HISTORIAL =?", arrayOf(fkHistorial.toString()))
    }

    fun borrarAmigo(fkAmigo1: Int, fkAmigo2: Int) {
        val db = this.writableDatabase
        db.delete(
            "amigos",
            "(($KEY_FK_AMIGO1 = ? AND $KEY_FK_AMIGO2 = ?) OR ($KEY_FK_AMIGO1 = ? AND $KEY_FK_AMIGO2 = ?))",
            arrayOf(
                fkAmigo1.toString(),
                fkAmigo2.toString(),
                fkAmigo2.toString(),
                fkAmigo1.toString()
            )
        )

    }

    //////////////////////////ACTUALIZAR ELEMENTOS//////////////////////////////////////

    // para actualizar nombre, correo, contraseña
    fun actualizarUsuario(usuario: Usuario) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_NOMBRE_USUARIO, usuario.nombre_usuario)
        datos.put(KEY_CORREO, usuario.correo_usuario)
        datos.put(KEY_CONTRASENIA, usuario.contrasenia_usuario)
        db.update(
            TABLE_USUARIOS,
            datos,
            "$KEY_ID_USUARIO=?",
            arrayOf(usuario.id_usuario.toString())
        )
    }

    // para actulizar veces jugado, fecha ultima vez jugado, anotaciones
    fun actualizarJuegoEnColeccion(juegoEnPropiedad: Coleccion) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(
            KEY_PRECIO_COMPRA, juegoEnPropiedad.precioCompra_coleccion
        )
        datos.put(
            KEY_FECHA_ULTIMA_VEZ_JUEGADO, juegoEnPropiedad.ultimaVezJugado_coleccion
        )
        datos.put(KEY_VECES_JUGADO, juegoEnPropiedad.vecesJugado_coleccion)
        datos.put(KEY_ANOTACION_PERSONAL, juegoEnPropiedad.anotacionPersonal_coleccion)
        datos.put(KEY_FK_USUARIO_COLECCIONA_JUEGO, juegoEnPropiedad.fk_usuario_tiene_coleccion)
        datos.put(KEY_FK_JUEGO_COLECCIONADO, juegoEnPropiedad.fk_juego_en_coleccion)
        db.update(
            TABLE_COLECCION,
            datos,
            "$KEY_ID_COLECCION=?",
            arrayOf(juegoEnPropiedad.id_coleccion.toString())
        )
    }

    fun actualizarObservaciones(fkUsuario: Int, fkJuego: Int, observacioens: String) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_ANOTACION_PERSONAL, observacioens)
        db.update(
            TABLE_COLECCION,
            datos,
            "$KEY_FK_USUARIO_COLECCIONA_JUEGO =? AND $KEY_FK_JUEGO_COLECCIONADO=?",
            arrayOf(fkUsuario.toString(), fkJuego.toString())
        )
    }

    ///////////////////////////INICAR SESIÓN///////////////////////////////////////////
    fun incioSesion(correo: String, contrasenia: String): Int {
        var idUsuario = -8
        val db = this.readableDatabase
        val query =
            "SELECT $KEY_ID_USUARIO FROM $TABLE_USUARIOS WHERE $KEY_CORREO =? AND $KEY_CONTRASENIA =?"

        var cursor: Cursor?
        cursor = db.rawQuery(query, arrayOf(correo, contrasenia))

        if (cursor != null && cursor.moveToFirst()) {
            var idCursor = cursor.getColumnIndex(KEY_ID_USUARIO)
            idUsuario = cursor.getInt(idCursor)
            return idUsuario
        } else {
            return idUsuario
        }
        cursor.close()
        db.close()
    }

    //////////////////////////OBTENER LISTAS ELEMENTOS/////////////////////////////////

    fun listaTodasMecanicas(): MutableList<Mecanica> {
        val todasMecanicas = mutableListOf<Mecanica>()
        val db = this.writableDatabase
        val sentencia = "SELECT * FROM $TABLE_MECANICAS"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val cursorIDMecanica = cursor.getColumnIndex(KEY_ID_MECANICA)
                val cursorNombreMecanica = cursor.getColumnIndex(KEY_NOMBRE_MECANICA)

                val mecanica = Mecanica(
                    id_mecanica = cursor.getInt(cursorIDMecanica),
                    nombreMecanica = cursor.getString(cursorNombreMecanica)
                )
                todasMecanicas.add(mecanica)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todasMecanicas
    }

    fun listaJuegosPropiedad(fkIdusuario: Int): MutableList<Coleccion> {
        val todosJuegosTiene = mutableListOf<Coleccion>()
        val db = this.writableDatabase
        val sentencia = "SELECT * FROM $TABLE_COLECCION WHERE $KEY_FK_USUARIO_COLECCIONA_JUEGO =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(fkIdusuario.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val cursorIdJuegoTiene = cursor.getColumnIndex(KEY_ID_COLECCION)
                val cursorPrecio = cursor.getColumnIndex(KEY_PRECIO_COMPRA)
                val cursorVecesJugado = cursor.getColumnIndex(KEY_PRECIO_COMPRA)
                val cursorUlitimaVexJugado = cursor.getColumnIndex(KEY_FECHA_ULTIMA_VEZ_JUEGADO)
                val cursorAnotaciones = cursor.getColumnIndex(KEY_ANOTACION_PERSONAL)
                val cursorFKIDJuego = cursor.getColumnIndex(KEY_FK_JUEGO_COLECCIONADO)

                val juegoEnPropiedad = Coleccion(
                    id_coleccion = cursor.getInt(cursorIdJuegoTiene),
                    precioCompra_coleccion = cursor.getDouble(cursorPrecio),
                    vecesJugado_coleccion = cursor.getInt(cursorVecesJugado),
                    ultimaVezJugado_coleccion = cursor.getString(cursorUlitimaVexJugado),
                    anotacionPersonal_coleccion = cursor.getString(cursorAnotaciones),
                    fk_juego_en_coleccion = cursor.getInt(cursorFKIDJuego),
                    fk_usuario_tiene_coleccion = fkIdusuario
                )
                todosJuegosTiene.add(juegoEnPropiedad)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todosJuegosTiene
    }

    fun listaJuegosPropiedadSiendoJuegosLoObtenido(fkIdusuario: Int): MutableList<Juego> {
        val todosJuegosTiene = mutableListOf<Juego>()
        val db = this.writableDatabase
        val sentencia =
            "SELECT * FROM $TABLE_JUEGOS WHERE $KEY_ID_JUEGO IN (SELECT $KEY_FK_JUEGO_COLECCIONADO FROM $TABLE_COLECCION WHERE $KEY_FK_USUARIO_COLECCIONA_JUEGO=?)"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(fkIdusuario.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val cursorIdJuego = cursor.getColumnIndex(KEY_ID_JUEGO)
                val curNombre = cursor.getColumnIndex(KEY_NOMBRE_JUEGO)
                val curDescripcion = cursor.getColumnIndex(KEY_DESCIPRCION_JUEGO)
                val curJugMinimo = cursor.getColumnIndex(KEY_NUMERO_JUGADORES_MINIMO)
                val curJugMaximo = cursor.getColumnIndex(KEY_NUMERO_JUGADORES_MAXIMO)
                val curDuracion = cursor.getColumnIndex(KEY_DURACION)


                val juego = Juego(
                    idJuego = cursor.getInt(cursorIdJuego),
                    nombreJuego = cursor.getString(curNombre),
                    descipcionJuegp = cursor.getString(curDescripcion),
                    minimoJugadoresJuego = cursor.getInt(curJugMinimo),
                    maximoJugadoresJuego = cursor.getInt(curJugMaximo),
                    duracionJuego = cursor.getInt(curDuracion)
                )
                todosJuegosTiene.add(juego)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todosJuegosTiene
    }

    fun listaTodasPartidas(fk_idUsuario: Int): MutableList<Historial> {
        val todasPartidasHistorial = mutableListOf<Historial>()
        val db = this.writableDatabase
        val sentencia = "SELECT * FROM $TABLE_HISTORIAL WHERE $KEY_FK_USUARIO_MONTA_PARTIDA =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(fk_idUsuario.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val cursorIdHiatorial = cursor.getColumnIndex(KEY_ID_HISTORIAL)
                val cursorNombre = cursor.getColumnIndex(KEY_NOMBRE_PARTIDA)
                val cursorNumeroPersonas = cursor.getColumnIndex(KEY_NUMERO_PERSONAS)
                val cursorFecha = cursor.getColumnIndex(KEY_FECHA_PARTIDA)

                val historial = Historial(
                    id_historial = cursor.getInt(cursorIdHiatorial),
                    nombre_historial = cursor.getString(cursorNombre),
                    fecha_historial = cursor.getString(cursorFecha),
                    numeroPersonas_historial = cursor.getInt(cursorNumeroPersonas),
                    usuario_historial = fk_idUsuario
                )
                todasPartidasHistorial.add(historial)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todasPartidasHistorial
    }

    fun listadoTodosJuegosPartidaMostrandoJuego(fk_idHistorial: Int): MutableList<Juego> {
        val todosJuegosUsados = mutableListOf<Juego>()
        val db = this.writableDatabase
        val sentencia =
            "SELECT * FROM $TABLE_JUEGOS WHERE $KEY_ID_JUEGO IN (SELECT $KEY_FK_JUEGO_JUEGOUSADO FROM $TABLE_JUEGOS_USADOS WHERE $KEY_FK_HISTORIAL =?)"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(fk_idHistorial.toString()))
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val idCur = cursor.getColumnIndex(KEY_ID_JUEGO)
                val nombreCur = cursor.getColumnIndex(KEY_NOMBRE_JUEGO)
                val descripcionCur = cursor.getColumnIndex(KEY_DESCIPRCION_JUEGO)
                val minJugadorCur = cursor.getColumnIndex(KEY_NUMERO_JUGADORES_MINIMO)
                val maxJugadorCur = cursor.getColumnIndex(KEY_NUMERO_JUGADORES_MAXIMO)
                val duracionCur = cursor.getColumnIndex(KEY_DURACION)

                val juego = Juego(
                    idJuego = cursor.getInt(idCur),
                    nombreJuego = cursor.getString(nombreCur),
                    descipcionJuegp = cursor.getString(descripcionCur),
                    minimoJugadoresJuego = cursor.getInt(minJugadorCur),
                    maximoJugadoresJuego = cursor.getInt(maxJugadorCur),
                    duracionJuego = cursor.getInt(duracionCur)
                )
                todosJuegosUsados.add(juego)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todosJuegosUsados
    }

    fun listadoTodosAmigosObteniendoElAmigo(fkAmigo1: Int): MutableList<Usuario> {
        val todosAmigos = mutableListOf<Usuario>()
        val db = this.readableDatabase

        val sentencia = """
        SELECT u.*
        FROM $TABLE_USUARIOS u
        INNER JOIN $TABLE_AMIGOS a 
        ON (u.$KEY_ID_USUARIO = a.$KEY_FK_AMIGO1 AND a.$KEY_FK_AMIGO2 = ?) 
        OR (u.$KEY_ID_USUARIO = a.$KEY_FK_AMIGO2 AND a.$KEY_FK_AMIGO1 = ?)
    """.trimIndent()

        val cursor = db.rawQuery(sentencia, arrayOf(fkAmigo1.toString(), fkAmigo1.toString()))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_USUARIO))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE_USUARIO))
                // Agrega más campos si tienes más columnas

                val usuario = Usuario(
                    id_usuario = id,
                    nombre_usuario = nombre,
                    correo_usuario = null,
                    contrasenia_usuario = null
                )
                todosAmigos.add(usuario)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return todosAmigos
    }

    fun listaMecanicasEnJuegoDadoJuego(fk_idjuego: Int): MutableList<String> {
        val mecanciasEnjuego = mutableListOf<String>()
        val db = this.readableDatabase
        val sentencia =
            "SELECT $KEY_NOMBRE_MECANICA FROM $TABLE_MECANICAS WHERE $KEY_ID_MECANICA IN(SELECT $KEY_FK_MECANICAS_MECANICASENJUEGO FROM $TABLE_MECANICAS_EN_JUEGO WHERE $KEY_FK_JUEGO_MECANICASENJUEGO =?)"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(fk_idjuego.toString()))
        if (cursor.moveToFirst()) {
            do {
                val nombreMecCur = cursor.getString(0)
                mecanciasEnjuego.add(nombreMecCur)
            } while (cursor.moveToNext())
        }

        return mecanciasEnjuego
    }

    fun listaTodosJuegosBBDD(): MutableList<Juego> {
        var todosLosJuegos = mutableListOf<Juego>()
        val db = this.readableDatabase
        val sentencia = "SELECT * FROM $TABLE_JUEGOS"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, null)

        if (cursor.moveToFirst()) {
            do {
                val idCur = cursor.getColumnIndex(KEY_ID_JUEGO)
                val nombreCur = cursor.getColumnIndex(KEY_NOMBRE_JUEGO)
                val descripcionCur = cursor.getColumnIndex(KEY_DESCIPRCION_JUEGO)
                val minJugCur = cursor.getColumnIndex(KEY_NUMERO_JUGADORES_MINIMO)
                val maxJugCur = cursor.getColumnIndex(KEY_NUMERO_JUGADORES_MAXIMO)
                val duracionCur = cursor.getColumnIndex(KEY_DURACION)

                val juego = Juego(
                    idJuego = cursor.getInt(idCur),
                    nombreJuego = cursor.getString(nombreCur),
                    descipcionJuegp = cursor.getString(descripcionCur),
                    minimoJugadoresJuego = cursor.getInt(minJugCur),
                    maximoJugadoresJuego = cursor.getInt(maxJugCur),
                    duracionJuego = cursor.getInt(duracionCur)
                )
                todosLosJuegos.add(juego)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todosLosJuegos
    }


    fun listaTodosJuegosYMecanicas(): MutableList<MecanicaEnJuego> {
        var todosJuegosConMencanicas = mutableListOf<MecanicaEnJuego>()
        val db = this.readableDatabase
        val sentencia = "SELECT * FROM $TABLE_MECANICAS_EN_JUEGO"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, null)

        if (cursor.moveToFirst()) {
            do {
                val idMecJug = cursor.getColumnIndex(KEY_ID_MECANICASENJUEGO)
                val fkMec = cursor.getColumnIndex(KEY_FK_MECANICAS_MECANICASENJUEGO)
                val fkJug = cursor.getColumnIndex(KEY_FK_JUEGO_MECANICASENJUEGO)

                val mecanicaEnJuego = MecanicaEnJuego(
                    id_mecanicaJuego = cursor.getInt(idMecJug),
                    fk_juego_mecanicaJuego = cursor.getInt(fkJug),
                    fk_mecanica_mecanicaJuego = cursor.getInt(fkMec)
                )

                todosJuegosConMencanicas.add(mecanicaEnJuego)

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todosJuegosConMencanicas
    }

    ///////////////////////////DETALLES CONCRETOS/////////////////////////////////////////
    // matid tonto- detalle amigo da una usuario asi que es detalle usuario
    fun detalleUsuario(idUsuario: Int): Usuario? {
        var usuario: Usuario? = null
        val db = this.readableDatabase

        val sentencia = "SELECT * FROM $TABLE_USUARIOS WHERE $KEY_ID_USUARIO =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(idUsuario.toString()))
        if (cursor.moveToFirst()) {
            val cursprCorreo = cursor.getColumnIndex(KEY_CORREO)
            val cursorContrasenia = cursor.getColumnIndex(KEY_CONTRASENIA)
            val cursorNombre = cursor.getColumnIndex(KEY_NOMBRE_USUARIO)

            usuario = Usuario(
                id_usuario = idUsuario,
                nombre_usuario = cursor.getString(cursorNombre),
                correo_usuario = cursor.getString(cursprCorreo),
                contrasenia_usuario = cursor.getString(cursorContrasenia)
            )
        }

        cursor.close()
        db.close()
        return usuario
    }

    fun detalleJuegoTiene(fkPropietario: Int, fkJuego: Int): Coleccion? {
        var juegoTiene: Coleccion? = null
        val db = this.readableDatabase
        val sententencia =
            "SELECT * FROM $TABLE_COLECCION WHERE $KEY_FK_USUARIO_COLECCIONA_JUEGO =? AND $KEY_FK_JUEGO_COLECCIONADO=?"
        var cursor: Cursor?
        cursor = db.rawQuery(sententencia, arrayOf(fkPropietario.toString(), fkJuego.toString()))
        if (cursor.moveToFirst()) {
            val idColeccionCursor = cursor.getColumnIndex(KEY_ID_COLECCION)
            val precioCompraCursor = cursor.getColumnIndex(KEY_PRECIO_COMPRA)
            val vecesJugadoCursor = cursor.getColumnIndex(KEY_VECES_JUGADO)
            val ultimaVezCursor = cursor.getColumnIndex(KEY_FECHA_ULTIMA_VEZ_JUEGADO)
            val anotacionesCurosr = cursor.getColumnIndex(KEY_ANOTACION_PERSONAL)
            val fkUsiarioCursor = cursor.getColumnIndex(KEY_FK_USUARIO_COLECCIONA_JUEGO)

            juegoTiene = Coleccion(
                id_coleccion = cursor.getInt(idColeccionCursor),
                precioCompra_coleccion = cursor.getDouble(precioCompraCursor),
                vecesJugado_coleccion = cursor.getInt(vecesJugadoCursor),
                ultimaVezJugado_coleccion = cursor.getString(ultimaVezCursor),
                anotacionPersonal_coleccion = cursor.getString(anotacionesCurosr),
                fk_usuario_tiene_coleccion = cursor.getInt(fkUsiarioCursor),
                fk_juego_en_coleccion = fkPropietario
            )
        }
        cursor.close()
        db.close()
        return juegoTiene
    }

    fun ulitmoJuegoJugado(idUsuario: Int): Juego? {
        val db = this.readableDatabase
        val sentencia =
            "SELECT J.* FROM $TABLE_JUEGOS J " +
                    "JOIN $TABLE_COLECCION C ON J.$KEY_ID_JUEGO = C.$KEY_FK_JUEGO_COLECCIONADO " +
                    "WHERE C.$KEY_FK_USUARIO_COLECCIONA_JUEGO = ? " +
                    "ORDER BY C.$KEY_FECHA_ULTIMA_VEZ_JUEGADO ASC LIMIT 1"
        val cursor = db.rawQuery(sentencia, arrayOf(idUsuario.toString()))

        var juego = Juego()
        if (cursor.moveToFirst()) {
            juego = Juego(
                idJuego = cursor.getInt(0),
                nombreJuego = cursor.getString(1),
                descipcionJuegp = cursor.getString(2),
                minimoJugadoresJuego = cursor.getInt(3),
                maximoJugadoresJuego = cursor.getInt(4),
                duracionJuego = cursor.getInt(5)
            )

        }
        return juego
    }

    fun detalleJuego(idJuego: Int): Juego? {
        var juego: Juego? = null
        val db = this.readableDatabase
        val sentencia = "SELECT * FROM $TABLE_JUEGOS WHERE $KEY_ID_JUEGO =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(idJuego.toString()))
        if (cursor.moveToFirst()) {
            val nombre = cursor.getColumnIndex(KEY_NOMBRE_JUEGO)
            val descipcion = cursor.getColumnIndex(KEY_DESCIPRCION_JUEGO)
            val minimoJadores = cursor.getColumnIndex(KEY_NUMERO_JUGADORES_MINIMO)
            val maximoJuagdor = cursor.getColumnIndex(KEY_NUMERO_JUGADORES_MAXIMO)
            val duracion = cursor.getColumnIndex(KEY_DURACION)

            juego = Juego(
                idJuego = idJuego,
                nombreJuego = cursor.getString(nombre),
                descipcionJuegp = cursor.getString(descipcion),
                minimoJugadoresJuego = cursor.getInt(minimoJadores),
                maximoJugadoresJuego = cursor.getInt(maximoJuagdor),
                duracionJuego = cursor.getInt(duracion)
            )
        }

        return juego
    }

    fun detalleHistorial(idHistorial: Int): Historial? {
        var historial: Historial? = null
        val db = this.readableDatabase
        val sentencia = "SELECT * FROM $TABLE_HISTORIAL WHERE $KEY_ID_HISTORIAL =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(idHistorial.toString()))
        if (cursor.moveToFirst()) {
            val curNombre = cursor.getColumnIndex(KEY_NOMBRE_PARTIDA)
            val cruFecha = cursor.getColumnIndex(KEY_FECHA_PARTIDA)
            val curNumeroPersonas = cursor.getColumnIndex(KEY_NUMERO_PERSONAS)
            val curFKusaurio = cursor.getColumnIndex(KEY_FK_USUARIO_MONTA_PARTIDA)

            historial = Historial(
                id_historial = idHistorial,
                nombre_historial = cursor.getString(curNombre),
                fecha_historial = cursor.getString(cruFecha),
                numeroPersonas_historial = cursor.getInt(curNumeroPersonas),
                usuario_historial = cursor.getInt(curFKusaurio)
            )
        }

        cursor.close()
        db.close()
        return historial
    }

    ///////////////////////////FILTRADO CONVOCAR ///////////////////////////////////////

    fun todaColeccionDelUsuario(idUsuario: Int): MutableList<Int> {
        val juegos = mutableListOf<Int>()
        val db = this.readableDatabase

        val query ="SELECT $KEY_FK_JUEGO_COLECCIONADO FROM $TABLE_COLECCION WHERE $KEY_FK_USUARIO_COLECCIONA_JUEGO = ?"

        val cursor = db.rawQuery(query, arrayOf(idUsuario.toString()))

        if (cursor.moveToFirst()) {
            do {
                val idJuego = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_FK_JUEGO_COLECCIONADO))
                juegos.add(idJuego)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return juegos
    }

    ///////////////////////////FILTRADO CONVOCAR ///////////////////////////////////////

    fun listadoJuegosFiltrados(
        idUsuario: Int,
        jugadoresVan: Int,
        horasJugar: Int,
        idMecanica: Int
    ): MutableList<Juego> {
        val juegos = mutableListOf<Juego>()

        val db = this.readableDatabase
        var duracionTotal = horasJugar * 60
        var duracionAcumulada = 0

        val query = """
        SELECT 
            j.*
        FROM 
            $TABLE_JUEGOS j
        JOIN 
            $TABLE_COLECCION c ON j.$KEY_ID_JUEGO = c.$KEY_FK_JUEGO_COLECCIONADO
        JOIN 
            $TABLE_MECANICAS_EN_JUEGO mj ON j.$KEY_ID_JUEGO = mj.$KEY_FK_JUEGO_MECANICASENJUEGO
        WHERE 
            c.$KEY_FK_USUARIO_COLECCIONA_JUEGO = ?
            AND j.$KEY_NUMERO_JUGADORES_MINIMO <= ?
            AND j.$KEY_NUMERO_JUGADORES_MAXIMO >= ?
            AND mj.$KEY_FK_MECANICAS_MECANICASENJUEGO = ?
        ORDER BY 
            c.$KEY_FECHA_ULTIMA_VEZ_JUEGADO DESC
    """

        val cursor = db.rawQuery(
            query, arrayOf(
                idUsuario.toString(),
                jugadoresVan.toString(),
                jugadoresVan.toString(),
                idMecanica.toString()
            )
        )

        if (cursor.moveToFirst()) {
            do {
                duracionAcumulada = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_DURACION))
                if (duracionAcumulada < duracionTotal) {
                    val juego = Juego(
                        idJuego = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_JUEGO)),
                        nombreJuego = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE_JUEGO)),
                        descipcionJuegp = cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                KEY_DESCIPRCION_JUEGO
                            )
                        ),
                        minimoJugadoresJuego = cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                KEY_NUMERO_JUGADORES_MINIMO
                            )
                        ),
                        maximoJugadoresJuego = cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                KEY_NUMERO_JUGADORES_MAXIMO
                            )
                        ),
                        duracionJuego = cursor.getInt(
                            cursor.getColumnIndexOrThrow(KEY_DURACION)
                        )
                    )
                    duracionAcumulada += juego.duracionJuego
                    juegos.add(juego)

                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        return juegos
    }

    ///////////////////////////FILTRADO JUGADORES ///////////////////////////////////////

    fun todaColeccionUsuarioPorNumJugadores(
        idUsuario: Int,
        jugadores: Int
    ): MutableList<Int> {
        val db = this.readableDatabase
        val listaIds = mutableListOf<Int>()

        val query = """
        SELECT 
            j.$KEY_ID_JUEGO
        FROM 
            $TABLE_JUEGOS j
        JOIN 
            $TABLE_COLECCION c ON j.$KEY_ID_JUEGO = c.$KEY_FK_JUEGO_COLECCIONADO
        WHERE 
            c.$KEY_FK_USUARIO_COLECCIONA_JUEGO = ?
            AND j.$KEY_NUMERO_JUGADORES_MINIMO <= ?
            AND j.$KEY_NUMERO_JUGADORES_MAXIMO >= ?
        ORDER BY 
            c.$KEY_FECHA_ULTIMA_VEZ_JUEGADO DESC
    """

        val cursor = db.rawQuery(
            query, arrayOf(
                idUsuario.toString(),
                jugadores.toString(),
                jugadores.toString()
            )
        )

        if (cursor.moveToFirst()) {
            do {
                val idJuego = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_JUEGO))
                listaIds.add(idJuego)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return listaIds
    }

    ///////////////////////////FILTRADO MECANICA ///////////////////////////////////////

    fun todaColecciónUsuarioPorMecanica(
        idUsuario: Int,
        idMecanica: Int
    ): MutableList<Int> {
        val db = this.readableDatabase
        val listaIds = mutableListOf<Int>()

        val query = """
        SELECT 
            j.$KEY_ID_JUEGO
        FROM 
            $TABLE_JUEGOS j
        JOIN 
            $TABLE_COLECCION c ON j.$KEY_ID_JUEGO = c.$KEY_FK_JUEGO_COLECCIONADO
        JOIN 
            $TABLE_MECANICAS_EN_JUEGO mj ON j.$KEY_ID_JUEGO = mj.$KEY_FK_JUEGO_MECANICASENJUEGO
        WHERE 
            c.$KEY_FK_USUARIO_COLECCIONA_JUEGO = ?
            AND mj.$KEY_FK_MECANICAS_MECANICASENJUEGO = ?
        ORDER BY 
            c.$KEY_FECHA_ULTIMA_VEZ_JUEGADO DESC
    """

        val cursor = db.rawQuery(
            query, arrayOf(
                idUsuario.toString(),
                idMecanica.toString()
            )
        )

        if (cursor.moveToFirst()) {
            do {
                val idJuego = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_JUEGO))
                listaIds.add(idJuego)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return listaIds
    }

    ///////////////////////////FILTRADO JUEGADORES Y MECANICA///////////////////////////

    fun todaColeccionUsuarioConMecanicaYJugadores(
        idUsuario: Int,
        jugadores: Int,
        idMecanica: Int
    ): MutableList<Int> {
        val db = this.readableDatabase
        val listaIds = mutableListOf<Int>()

        val query = """
        SELECT 
            j.$KEY_ID_JUEGO
        FROM 
            $TABLE_JUEGOS j
        JOIN 
            $TABLE_COLECCION c ON j.$KEY_ID_JUEGO = c.$KEY_FK_JUEGO_COLECCIONADO
        JOIN 
            $TABLE_MECANICAS_EN_JUEGO mj ON j.$KEY_ID_JUEGO = mj.$KEY_FK_JUEGO_MECANICASENJUEGO
        WHERE 
            c.$KEY_FK_USUARIO_COLECCIONA_JUEGO = ?
            AND mj.$KEY_FK_MECANICAS_MECANICASENJUEGO = ?
            AND j.$KEY_NUMERO_JUGADORES_MINIMO <= ?
            AND j.$KEY_NUMERO_JUGADORES_MAXIMO >= ?
        ORDER BY 
            c.$KEY_FECHA_ULTIMA_VEZ_JUEGADO DESC
    """

        val cursor = db.rawQuery(
            query, arrayOf(
                idUsuario.toString(),
                idMecanica.toString(),
                jugadores.toString(),
                jugadores.toString()
            )
        )

        if (cursor.moveToFirst()) {
            do {
                val idJuego = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_JUEGO))
                listaIds.add(idJuego)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return listaIds
    }

    ///////////////////////////AGRUPACIONES NUMERICAS///////////////////////////////////
    fun agrupacionJuegosEnUso(fkIdHistorial: Int): Int? {
        var cantidad: Int? = null
        val db = this.readableDatabase
        val sentencia =
            "SELECT COUNT($KEY_ID_JUEGO_USADO) FROM $TABLE_JUEGOS_USADOS WHERE $KEY_FK_HISTORIAL =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(fkIdHistorial.toString()))
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0)
        }
        return cantidad
    }

    fun totalJuegosEnColeccion(fkPropietario: Int): Int? {
        var cantidad: Int? = null
        val db = this.readableDatabase
        val setencia =
            "SELECT COUNT($KEY_FK_USUARIO_COLECCIONA_JUEGO) FROM $TABLE_COLECCION WHERE $KEY_FK_USUARIO_COLECCIONA_JUEGO =?"
        var cursor: Cursor?
        cursor = db.rawQuery(setencia, arrayOf(fkPropietario.toString()))
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0)
        }
        return cantidad
    }

    ///////////////////////////BUSCAR POR NOMBRE///////////////////////////////////////
    fun buscarJuegoNombre(nombreBusqueda: String): Juego? {
        var juego: Juego? = null
        val db = this.readableDatabase

        val sentencia = "SELECT * FROM $TABLE_JUEGOS WHERE $KEY_NOMBRE_JUEGO LIKE ?"
        val cursor = db.rawQuery(sentencia, arrayOf("%$nombreBusqueda%"))

        if (cursor.moveToFirst()) {
            // Aquí asumes que la clase Juego tiene estos campos (ajusta según tu modelo)
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_JUEGO))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE_JUEGO))
            val descripcio = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCIPRCION_JUEGO))
            val minimo = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NUMERO_JUGADORES_MINIMO))
            val maximo = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NUMERO_JUGADORES_MAXIMO))
            val duracion = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_DURACION))

            juego = Juego(
                idJuego = id,
                nombreJuego = nombre,
                descipcionJuegp = descripcio,
                minimoJugadoresJuego = minimo,
                maximoJugadoresJuego = maximo,
                duracionJuego = duracion
            )
        }

        cursor.close()
        db.close()
        return juego
    }

    ///////////////////////////BUSCAR POR NOMBRE- obteniendo un listado////////////////
    fun buscarJuegoNombreListado(nombreBusqueda: String): List<Juego> {
        val listaJuegos = mutableListOf<Juego>()
        val db = this.readableDatabase

        val sentencia = "SELECT * FROM $TABLE_JUEGOS WHERE $KEY_NOMBRE_JUEGO LIKE ?"
        val cursor = db.rawQuery(sentencia, arrayOf("%$nombreBusqueda%"))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_JUEGO))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE_JUEGO))
                val descripcion =
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCIPRCION_JUEGO))
                val minimo =
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NUMERO_JUGADORES_MINIMO))
                val maximo =
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NUMERO_JUGADORES_MAXIMO))
                val duracion = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_DURACION))

                val juego = Juego(
                    idJuego = id,
                    nombreJuego = nombre,
                    descipcionJuegp = descripcion,
                    minimoJugadoresJuego = minimo,
                    maximoJugadoresJuego = maximo,
                    duracionJuego = duracion
                )

                listaJuegos.add(juego)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return listaJuegos
    }

    ///////////////////////////COMPROBAR SI HAY REGISTROS//////////////////////////////
    fun tienesAmigos(idUsuario: Int): Boolean {
        val db = this.readableDatabase
        val sentencia =
            "SELECT 1 FROM $TABLE_AMIGOS WHERE $KEY_FK_AMIGO1 = ? OR $KEY_FK_AMIGO2 = ? LIMIT 1"
        val cursor = db.rawQuery(sentencia, arrayOf(idUsuario.toString(), idUsuario.toString()))
        val tienes = cursor.moveToFirst()
        cursor.close()
        return tienes
    }

    fun comrpobarSiSonAmigos(fkAmigo1: Int, fkAmigo2: Int): Boolean {
        val db = this.readableDatabase
        val sentencia =
            "SELECT 1 FROM $TABLE_AMIGOS WHERE ($KEY_FK_AMIGO1 = ? AND $KEY_FK_AMIGO2 = ?) OR ($KEY_FK_AMIGO1 =? AND $KEY_FK_AMIGO2=?)"
        val cursor = db.rawQuery(
            sentencia,
            arrayOf(
                fkAmigo1.toString(),
                fkAmigo2.toString(),
                fkAmigo2.toString(),
                fkAmigo1.toString()
            )
        )
        val sonAmigos = cursor.moveToFirst()
        cursor.close()
        return sonAmigos
    }

    fun comprobarJuegoEnColeccion(fkJuego: Int, fkUsuario: Int): Boolean {
        val db = this.readableDatabase
        val query =
            "SELECT * FROM $TABLE_COLECCION WHERE $KEY_FK_USUARIO_COLECCIONA_JUEGO = ? AND $KEY_FK_JUEGO_COLECCIONADO = ?"
        val cursor = db.rawQuery(query, arrayOf(fkUsuario.toString(), fkJuego.toString()))
        val existe = cursor.moveToFirst()
        cursor.close()
        db.close()
        return existe
    }

    fun deFechaATexto(localDate: LocalDate?): String {
        if (localDate == null) return "Nunca"
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        return formatter.format(date)
    }

    fun deTextoAFecha(textoFechado: String): LocalDate? {
        return try {
            if (textoFechado != "Nunca") {
                val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                LocalDate.parse(textoFechado, formatoFecha)
            } else {
                null // Equivalente a no haber jugado nunca, coherente con la otra función
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    ///////////////////////////DATOS MINIMOS///////////////////////////////////////////
    fun datosMninimos() {
        val todasMecanicas =
            listOf(
                Mecanica(null, "Azar"), //1
                Mecanica(null, "Bazas"),//2
                Mecanica(null, "Colección de sets"),//3
                Mecanica(null, "Colocación de losetas"),//4
                Mecanica(null, "Colocación de trabajadores"),//5
                Mecanica(null, "Construcción de mazo"),//6
                Mecanica(null, "Cooperativo"),//7
                Mecanica(null, "Destreza"),//8
                Mecanica(null, "Deducción/escape"),//9
                Mecanica(null, "Estrategia"),//10//
                Mecanica(null, "Exploración/aventuera"),//11
                Mecanica(null, "Gestión de acciones"),//12
                Mecanica(null, "Gestión de recursos"),//13
                Mecanica(null, "Lanzamiento de dados"),//14
                Mecanica(null, "Memoria"),//15
                Mecanica(null, "Narrativo/imaginativo"),//16
                Mecanica(null, "Puzle"),//17
                Mecanica(null, "Roles"),//18
                Mecanica(null, "Roles ocultos"),//19
                Mecanica(null, "Roll & write"),//20
                Mecanica(null, "Gestión de cartas"),//21
                Mecanica(null, "Colocación de cartas"),//22
                Mecanica(null, "Colocación de dados")//23
            )

        // crear las mecanicas
        todasMecanicas.forEach { mecanica: Mecanica -> crearMecancia(mecanica) }

        val algunosJuegos = listOf(
            Juego(
                null,
                "Flamecraft",
                "Flamecraft es un juego de tablero en el que asumiréis el papel de Guardallamas, encargados de conseguir que los Dragones Artesanos lancen sus llamas especializadas para crear cosas maravillosas. Estos dragones son muy deseados por los comerciantes, ya que les permitirán aumentar las ventas entre sus clientes. El jugador que haya conseguido mayor reputación como Guardallamas será el vencedor de la partida.",
                1,
                5,
                60
            ),
            Juego(
                null,
                "Arborea",
                "Arbórea es un juego de tablero donde serás un espíritu que guía a los aldeanos para sanar y que el paisaje crezca. Para ello, les enviarás a peregrinaciones y construirás tu ecosistema personal. Durante el juego, los jugadores colocarán trabajadores en vías de acción, luego las vías avanzarán y los jugadores se moverán con ellas. Cuanto más se muevan las vías, mejores serán las recompensas que pueden ayudar a los jugadores. Estas recompensas ayudan a contribuir a la ruta de recursos compartidos, hacer ofrendas a los Sabios para invertir en regalos o atraer animales a la tierra de Arbórea. ",
                1,
                5,
                120
            ),
            Juego(
                null,
                "Rebel Princess",
                "Rebel Princess es un juego de cartas que se desarrolla a lo largo de cinco rondas, representando los cinco días de una fiesta. Se sigue la mecánica general de los juegos de gbazas, pero en cada ronda se introduce una regla especial que hace que cada partida sea totalmente diferente. Cuidado con los príncipes azules y con las ranas encantadas, porque traerán proposiciones matrimoniales que no te gustarán. El participante con menos proposiciones matrimoniales al finalizar la partida será el vencedor.",
                3,
                6,
                40
            ),
            Juego(
                null,
                "Jungle Speed",
                "En Jungle Speed tienes que quedarte sin cartas.\n" +
                        "\n" +
                        "No hay tiempo para pensar. Enseña tu carta y no des oportunidad a los demás. ¿Ya hay otra figura tribal idéntica en la mesa? Vuela a coger el Tótem. Si escuchas una carcajada, tu impaciencia te ha jugado una mala pasada, has perdido y otra vez a empezar: te llevas todas las cartas descubiertas por el resto de las manos durante la partida. Y no te enfades, el buen ambiente es esencial. Si no puedes evitar picarte, reta a las manos otra vez. Cuanto más corta sea la partida, más disfrutarás.",
                3,
                8,
                15
            ),
            Juego(
                null,
                "Duel Books",
                "En Duel Books te enfrentaras contra tus amigos con un libro. En este emocionamente juego de datos y capítulos cada jugador lanzará un dado para escoger el capítulo con el que jugar y ¡empezará el duelo! Empezando por el protagonista y se iran resolviendo las páginas hasta que solo quede un vencedor. Preparate para descrubir un juego como nunca habías visto.",
                2,
                6,
                20
            )
        )
        algunosJuegos.forEach { juego: Juego -> crearJuego(juego) }

        val mecanicasEnJuegos = listOf(
            // Flamecraft - idJuego = 1
            MecanicaEnJuego(
                null,
                fk_mecanica_mecanicaJuego = 13,
                fk_juego_mecanicaJuego = 1
            ), // Gestión de recursos
            MecanicaEnJuego(
                null,
                fk_mecanica_mecanicaJuego = 5,
                fk_juego_mecanicaJuego = 1
            ),  // Colocación de trabajadores

            // Arborea - idJuego = 2
            MecanicaEnJuego(
                null,
                fk_mecanica_mecanicaJuego = 4,
                fk_juego_mecanicaJuego = 2
            ),  // Colocación de trabajadores
            MecanicaEnJuego(
                null,
                fk_mecanica_mecanicaJuego = 11,
                fk_juego_mecanicaJuego = 2
            ), // Gestión de acciones

            // Rebel Princess - idJuego = 3
            MecanicaEnJuego(
                null,
                fk_mecanica_mecanicaJuego = 2,
                fk_juego_mecanicaJuego = 3
            ),  // Bazas

            // Jungle Speed - idJuego = 4
            MecanicaEnJuego(
                null,
                fk_mecanica_mecanicaJuego = 7,
                fk_juego_mecanicaJuego = 4
            )   // Destreza
        )
        mecanicasEnJuegos.forEach { mecanicaEnJuego: MecanicaEnJuego ->
            crearMecanciaEnJuego(
                mecanicaEnJuego
            )
        }

        val usuarios = listOf(
            Usuario(null, "a", "a@a.aa", "a"),
            Usuario(null, "b", "b@b.bb", "b"),
            Usuario(null, "admin", "admin", "admin")
        )
        usuarios.forEach { usuarios: Usuario -> crearUsuario(usuarios) }

        val usuarioConJuegos = listOf(
            Coleccion(null, 15.0, 1, null, "intimisimi", 1, 1),
            Coleccion(null, 30.0, 12, null, "ajshdfa", 1, 2),
            Coleccion(null, 90.0, 2, null, "", 1, 4),
            Coleccion(null, 12.0, 2, null, "", 2, 4),
            Coleccion(null, 90.0, 2, null, "", 2, 2),
            Coleccion(null, 70.0, 4, null, "", 3, 5),
            Coleccion(null, 90.0, 6, null, "", 3, 4),
            Coleccion(null, 60.0, 2, null, "", 3, 3)
        )
        usuarioConJuegos.forEach { usuarioConJuegos: Coleccion ->
            crearJuegoEnPropiedad(
                usuarioConJuegos
            )
        }

        val historialPartidas = listOf(
            Historial(null, "Veneno pa tu pieeeeel", null, 2, 2),
            Historial(null, "ahora vas y lo juegas", null, 5, 2),
            Historial(null, "los cojones", null, 3, 2),
            Historial(null, "polis bien", null, 15, 3),
            Historial(null, "poliplatos de placertea", null, 3, 3),
            Historial(null, "tu y quien mas?", null, 5, 1),
            Historial(null, "aaaaaah... NO!", null, 4, 3)
        )
        historialPartidas.forEach { historial: Historial -> crearHistorial(historial) }

        val juegosUsados = listOf(
            JuegoUsado(null, 1, 1),
            JuegoUsado(null, 2, 2),
            JuegoUsado(null, 3, 3),
            JuegoUsado(null, 4, 4),
            JuegoUsado(null, 5, 5),
            JuegoUsado(null, 2, 6),
            JuegoUsado(null, 1, 7)
        )
        juegosUsados.forEach { juegoUsado: JuegoUsado -> crearJuegoEnUso(juegoUsado) }

        val amigos = listOf(Amigo(null, 1, 2), Amigo(null, 1, 3), Amigo(null, 3, 2))
        amigos.forEach { amigo: Amigo -> crearAmigos(amigo) }

    }


}
