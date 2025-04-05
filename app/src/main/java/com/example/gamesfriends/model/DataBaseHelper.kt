package com.example.gamesfriends.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.gamesfriends.model.datos.Historial
import com.example.gamesfriends.model.datos.Juego
import com.example.gamesfriends.model.datos.Coleccion
import com.example.gamesfriends.model.datos.JuegoUsado
import com.example.gamesfriends.model.datos.Mecanica
import com.example.gamesfriends.model.datos.MecanicaEnJuego
import com.example.gamesfriends.model.datos.Usuario
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
        private const val TABLE_COLECCION = "Juegos En Propiedad"
        private const val KEY_ID_COLECCION = "JuedoPropiedadID"
        private const val KEY_FK_USUARIO_TIENE_JUEGO =
            "FK Propietario_ID"// id del juegador que lo tiene
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
        private const val KEY_DESCIPRCION_JUEGO = "Descripción"
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
        private const val KEY_NUMERO_PERSONAS =
            "Número Jugadores"// cuando exista el online esto sera una fk
        private const val KEY_FK_USUARIO_MONTA_PARTIDA = "FK Usuario Organizador"

        //TABLA DE JUEGOS LLEVADOS A LA PARTIDA
        /*
        Al ser los juegos usando un array de los mismos se define en otra tabla
         */
        val TABLE_JUEGOS_USADOS = "Tabla juegos llevados a la partida"
        val KEY_ID_JUEGO_USADO = "id juegos llevados"
        val KEY_FK_JUEGO_JUEGOUSADO = "id del juego llevado"
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
                "$KEY_DESCIPRCION_JUEGO TEXT NOT NULL," +
                "$KEY_NUMERO_JUGADORES_MAXIMO INTEGER, " +
                "$KEY_DURACION INTEGER)")

        val crearTablaMecanicas = ("CREATE TABLE IF NOT EXISTS $TABLE_MECANICAS(" +
                "$KEY_ID_MECANICA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_NOMBRE_MECANICA TEXT NOT NULL)")

        val crearTablaJuegosPropiedad = ("CREATE TABLE IF NOT EXISTS $TABLE_COLECCION(" +
                "$KEY_ID_COLECCION INTEGER PRIMARY KEY, " +
                "$KEY_FK_USUARIO_TIENE_JUEGO INTEGER," +
                "$KEY_FK_JUEGO_TIENE INTEGER, " +
                "$KEY_FECHA_ULTIMA_VEZ_JUEGADO TEXT," +
                "$KEY_VECES_JUGADO INTEGER," +
                "$KEY_ANOTACION_PERSONAL TEXT," +
                "$KEY_PRECIO_COMPRA REAL NOT NULL," +
                "FOREIGN KEY($KEY_FK_USUARIO_TIENE_JUEGO) REFERENCES $TABLE_USUARIOS($KEY_ID_USUARIO))," +
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
                "$KEY_NUMERO_PERSONAS INTEGER +" +
                "$KEY_FK_USUARIO_MONTA_PARTIDA INTEGER," +
                "FOREIGN KEY ($KEY_FK_USUARIO_MONTA_PARTIDA) REFERENCES $TABLE_USUARIOS($KEY_ID_USUARIO))")

        val crearTablaJuegosUsados = ("CREATE TABLE IF NOT EXISTS $TABLE_JUEGOS_USADOS (" +
                "$KEY_ID_JUEGO_USADO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_FK_HISTORIAL INTEGER NOT NULL, " +
                "$KEY_FK_JUEGO_JUEGOUSADO INTEGER NOT NULL, " +
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
            db.execSQL("DROP TABLE IF EXISTS $TABLE_COLECCION")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_MECANICAS_EN_JUEGO")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORIAL")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_JUEGOS_USADOS")
            //db.execSQL("DROP TABLE IF EXISTS $TABLE_AMIGOS")

            onCreate(db)
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////
    //////////////////////////CREAR NUEVOS ELEMENTOS///////////////////////////////////

    fun crearUsuario(usuario: Usuario) {

        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_NOMBRE_USUARIO, usuario.nombre_usuario)
        datos.put(KEY_CORREO, usuario.correo_usuario)
        datos.put(KEY_CONTRASENIA, usuario.contrasenia_usuario)
        db.insert(TABLE_USUARIOS, null, datos)

    }

    fun crearJuego(juego: Juego) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_NOMBRE_JUEGO, juego.nombreJuego)
        datos.put(KEY_DESCIPRCION_JUEGO, juego.descipcionJuegp)
        datos.put(
            KEY_DURACION, juego.duracionJuego
        )
        datos.put(
            KEY_NUMERO_JUGADORES_MINIMO, juego.minimoJugadoresJuego
        )
        datos.put(
            KEY_NUMERO_JUGADORES_MAXIMO, juego.maximoJugadoresJuego
        )

        db.insert(TABLE_JUEGOS, null, datos)
    }

    fun crecarMecancia(mecanica: Mecanica) {
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
            deFechaATexto(juegoEnPropiedad.ultimaVezJugado_coleccion)
        )
        datos.put(KEY_VECES_JUGADO, juegoEnPropiedad.vecesJugado_coleccion)
        datos.put(KEY_ANOTACION_PERSONAL, juegoEnPropiedad.anotacionPersonal_coleccion)
        datos.put(KEY_FK_USUARIO_TIENE_JUEGO, juegoEnPropiedad.fk_usuario_tiene_coleccion)
        datos.put(KEY_FK_JUEGO_TIENE, juegoEnPropiedad.fk_juego_en_coleccion)

        db.insert(TABLE_COLECCION, null, datos)
    }

    fun crearMecanciaEnJuego(mecanicaEnJuego: MecanicaEnJuego) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_FK_MECANICAS_MECANICASENJUEGO, mecanicaEnJuego.fk_mecanica_mecanicaJuego)
        datos.put(KEY_FK_JUEGO_MECANICASENJUEGO, mecanicaEnJuego.fk_juego_mecanicaJuego)

        db.insert(TABLE_MECANICAS_EN_JUEGO, null, datos)
    }

    fun crearHistorial(historial: Historial) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_NOMBRE_PARTIDA, historial.nombre_historial)
        datos.put(KEY_FECHA_PARTIDA, deFechaATexto(historial.fecha_historial))
        datos.put(KEY_NUMERO_PERSONAS, historial.numeroPersonas_historial)
        datos.put(KEY_FK_USUARIO_MONTA_PARTIDA, historial.usuario_historial)


        db.insert(TABLE_HISTORIAL, null, datos)
    }

    fun crearJuegoEnUso(juegoUsado: JuegoUsado) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(KEY_FK_JUEGO_JUEGOUSADO, juegoUsado.fkJuegoJuegousado)
        datos.put(KEY_FK_HISTORIAL, juegoUsado.fkHistorialJuegousado)

        db.insert(TABLE_JUEGOS_USADOS, null, datos)
    }

    //////////////////////////BORRAR ELEMENTOS/////////////////////////////////////////

    fun borrarUsuario(idUsuario: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_USUARIOS, "$KEY_ID_USUARIO =?", arrayOf(idUsuario.toString()))
    }

    fun borrarJuegoDeJuegosTiene(idJuegoTiene: Int) {
        val db = this.writableDatabase
        db.delete(
            TABLE_COLECCION,
            "$KEY_ID_COLECCION =?",
            arrayOf(idJuegoTiene.toString())
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
    fun actualizarJuego(juegoEnPropiedad: Coleccion) {
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(
            KEY_PRECIO_COMPRA, juegoEnPropiedad.precioCompra_coleccion
        )
        datos.put(
            KEY_FECHA_ULTIMA_VEZ_JUEGADO,
            deFechaATexto(juegoEnPropiedad.ultimaVezJugado_coleccion)
        )
        datos.put(KEY_VECES_JUGADO, juegoEnPropiedad.vecesJugado_coleccion)
        datos.put(KEY_ANOTACION_PERSONAL, juegoEnPropiedad.anotacionPersonal_coleccion)
        datos.put(KEY_FK_USUARIO_TIENE_JUEGO, juegoEnPropiedad.fk_usuario_tiene_coleccion)
        datos.put(KEY_FK_JUEGO_TIENE, juegoEnPropiedad.fk_juego_en_coleccion)
        db.update(
            TABLE_COLECCION,
            datos,
            "$KEY_ID_COLECCION=?",
            arrayOf(juegoEnPropiedad.id_coleccion.toString())
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
                listaTodasMecanicas().add(mecanica)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todasMecanicas
    }

    fun listaJuegosPropiedad(fkIdusuario: Int): MutableList<Coleccion> {
        val todosJuegosTiene = mutableListOf<Coleccion>()
        val db = this.writableDatabase
        val sentencia = "SELECT * FROM $TABLE_COLECCION WHERE $KEY_FK_USUARIO_TIENE_JUEGO =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(fkIdusuario.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val cursorIdJuegoTiene = cursor.getColumnIndex(KEY_ID_COLECCION)
                val cursorPrecio = cursor.getColumnIndex(KEY_PRECIO_COMPRA)
                val cursorVecesJugado = cursor.getColumnIndex(KEY_PRECIO_COMPRA)
                val cursorUlitimaVexJugado = cursor.getColumnIndex(KEY_FECHA_ULTIMA_VEZ_JUEGADO)
                val cursorAnotaciones = cursor.getColumnIndex(KEY_ANOTACION_PERSONAL)
                val cursorFKIDJuego = cursor.getColumnIndex(KEY_FK_JUEGO_TIENE)

                val juegoEnPropiedad = Coleccion(
                    id_coleccion = cursor.getInt(cursorIdJuegoTiene),
                    precioCompra_coleccion = cursor.getDouble(cursorPrecio),
                    vecesJugado_coleccion = cursor.getInt(cursorVecesJugado),
                    ultimaVezJugado_coleccion = deTextoAFecha(
                        cursor.getString(
                            cursorUlitimaVexJugado
                        )
                    ),
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
                    fecha_historial = deTextoAFecha(cursor.getString(cursorFecha)),
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

    fun listadoTodosJuegosPartida(fk_idHistorial: Int): MutableList<JuegoUsado> {
        val todosJuegosUsados = mutableListOf<JuegoUsado>()
        val db = this.writableDatabase
        val sentencia = "SELECT * FROM $TABLE_JUEGOS_USADOS WHERE $KEY_FK_HISTORIAL =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sentencia, arrayOf(fk_idHistorial.toString()))
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val cursorIDJuegoUsado = cursor.getColumnIndex(KEY_ID_JUEGO_USADO)
                val cursorFKjuego = cursor.getColumnIndex(KEY_FK_JUEGO_JUEGOUSADO)

                val juegoUsaod = JuegoUsado(
                    idJuegoUsado = cursor.getInt(cursorIDJuegoUsado),
                    fkJuegoJuegousado = cursor.getInt(cursorFKjuego),
                    fkHistorialJuegousado = fk_idHistorial
                )
                todosJuegosUsados.add((juegoUsaod))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todosJuegosUsados
    }

    ///////////////////////////DETALLES CONCRETOS/////////////////////////////////////////

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

    fun detalleJuegoTiene(idJuegoTiene: Int): Coleccion? {
        var juegoTiene: Coleccion? = null
        val db = this.readableDatabase
        val sententencia = "SELECT * FROM $TABLE_COLECCION WHERE $KEY_FK_USUARIO_TIENE_JUEGO =?"
        var cursor: Cursor?
        cursor = db.rawQuery(sententencia, arrayOf(idJuegoTiene.toString()))
        if (cursor.moveToFirst()) {
            val idColeccionCursor = cursor.getColumnIndex(KEY_ID_COLECCION)
            val precioCompraCursor = cursor.getColumnIndex(KEY_PRECIO_COMPRA)
            val vecesJugadoCursor = cursor.getColumnIndex(KEY_VECES_JUGADO)
            val ultimaVezCursor = cursor.getColumnIndex(KEY_FECHA_ULTIMA_VEZ_JUEGADO)
            val anotacionesCurosr = cursor.getColumnIndex(KEY_ANOTACION_PERSONAL)
            val fkUsiarioCursor = cursor.getColumnIndex(KEY_FK_USUARIO_TIENE_JUEGO)

            juegoTiene = Coleccion(
                id_coleccion = cursor.getInt(idColeccionCursor),
                precioCompra_coleccion = cursor.getDouble(precioCompraCursor),
                vecesJugado_coleccion = cursor.getInt(vecesJugadoCursor),
                ultimaVezJugado_coleccion = deTextoAFecha(cursor.getString(ultimaVezCursor)),
                anotacionPersonal_coleccion = cursor.getString(anotacionesCurosr),
                fk_usuario_tiene_coleccion = cursor.getInt(fkUsiarioCursor),
                fk_juego_en_coleccion = idJuegoTiene
            )
        }
        cursor.close()
        db.close()
        return juegoTiene
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
                fecha_historial = deTextoAFecha(cursor.getString(cruFecha)),
                numeroPersonas_historial = cursor.getInt(curNumeroPersonas),
                usuario_historial = cursor.getInt(curFKusaurio)
            )
        }

        cursor.close()
        db.close()
        return historial
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

    ///////////////////////////BUSCAR POR NOMBRE- obteniendo un listado///////////////////////////////////////
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

    ///////////////////////////FECHAS GESTOR///////////////////////////////////////////
    // conversor de fechas
    fun deFechaATexto(fecha: LocalDate?): String {
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
