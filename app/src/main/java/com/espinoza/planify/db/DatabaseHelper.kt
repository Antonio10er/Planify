package com.espinoza.planify.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.espinoza.planify.models.Asignatura

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Planify.db"

        const val TABLA_ASIGNATURAS = "tabla_asignaturas"
        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_DIA = "dia"
        const val COL_HORA_INICIO = "horaInicio"
        const val COL_HORA_FIN = "horaFin"
        const val COL_AULA = "aula"
        const val COL_PROFESOR = "profesor"
        const val COL_NOTA = "nota"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSQL = """
            CREATE TABLE $TABLA_ASIGNATURAS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT,
                $COL_DIA TEXT,
                $COL_HORA_INICIO TEXT,
                $COL_HORA_FIN TEXT,
                $COL_AULA TEXT,
                $COL_PROFESOR TEXT,
                $COL_NOTA TEXT
            )
        """.trimIndent()

        db?.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLA_ASIGNATURAS")
        onCreate(db)
    }

    fun insertarAsignatura(asignatura: Asignatura): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put(COL_NOMBRE, asignatura.nombre)
            put(COL_DIA, asignatura.dia)
            put(COL_HORA_INICIO, asignatura.horaInicio)
            put(COL_HORA_FIN, asignatura.horaFin)
            put(COL_AULA, asignatura.aula)
            put(COL_PROFESOR, asignatura.profesor)
            put(COL_NOTA, asignatura.nota)
        }

        val resultado = db.insert(TABLA_ASIGNATURAS, null, contentValues)

        db.close()
        return resultado
    }

    fun obtenerTodasLasAsignaturas(): MutableList<Asignatura> {
        val listaAsignaturas = mutableListOf<Asignatura>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLA_ASIGNATURAS"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(COL_ID)
                val nombreIndex = cursor.getColumnIndex(COL_NOMBRE)
                val diaIndex = cursor.getColumnIndex(COL_DIA)
                val horaInicioIndex = cursor.getColumnIndex(COL_HORA_INICIO)
                val horaFinIndex = cursor.getColumnIndex(COL_HORA_FIN)
                val aulaIndex = cursor.getColumnIndex(COL_AULA)
                val profesorIndex = cursor.getColumnIndex(COL_PROFESOR)
                val notaIndex = cursor.getColumnIndex(COL_NOTA)

                val asignatura = Asignatura(
                    id = if (idIndex != -1) cursor.getInt(idIndex) else 0,
                    nombre = if (nombreIndex != -1) cursor.getString(nombreIndex) else "",
                    dia = if (diaIndex != -1) cursor.getString(diaIndex) else "",
                    horaInicio = if (horaInicioIndex != -1) cursor.getString(horaInicioIndex) else "",
                    horaFin = if (horaFinIndex != -1) cursor.getString(horaFinIndex) else "",
                    aula = if (aulaIndex != -1) cursor.getString(aulaIndex) else null,
                    profesor = if (profesorIndex != -1) cursor.getString(profesorIndex) else null,
                    nota = if (notaIndex != -1) cursor.getString(notaIndex) else null
                )
                listaAsignaturas.add(asignatura)

            } while (cursor.moveToNext()) // Pasamos a la siguiente fila, si existe.
        }
        cursor.close()
        db.close()

        return listaAsignaturas
    }

    fun eliminarAsignatura(idAsignatura: Int): Int {
        val db = this.writableDatabase
        val resultado = db.delete(TABLA_ASIGNATURAS, "$COL_ID = ?", arrayOf(idAsignatura.toString()))
        db.close()

        return resultado
    }

    fun obtenerAsignaturasDelDia(nombreDia: String): List<Asignatura> {
        val listaAsignaturas = mutableListOf<Asignatura>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLA_ASIGNATURAS WHERE $COL_DIA = ?"
        val cursor = db.rawQuery(query, arrayOf(nombreDia))

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(COL_ID)
                val nombreIndex = cursor.getColumnIndex(COL_NOMBRE)
                val diaIndex = cursor.getColumnIndex(COL_DIA)
                val horaInicioIndex = cursor.getColumnIndex(COL_HORA_INICIO)
                val horaFinIndex = cursor.getColumnIndex(COL_HORA_FIN)
                val aulaIndex = cursor.getColumnIndex(COL_AULA)
                val profesorIndex = cursor.getColumnIndex(COL_PROFESOR)
                val notaIndex = cursor.getColumnIndex(COL_NOTA)

                val asignatura = Asignatura(
                    id = if (idIndex != -1) cursor.getInt(idIndex) else 0,
                    nombre = if (nombreIndex != -1) cursor.getString(nombreIndex) else "",
                    dia = if (diaIndex != -1) cursor.getString(diaIndex) else "",
                    horaInicio = if (horaInicioIndex != -1) cursor.getString(horaInicioIndex) else "",
                    horaFin = if (horaFinIndex != -1) cursor.getString(horaFinIndex) else "",
                    aula = if (aulaIndex != -1) cursor.getString(aulaIndex) else null,
                    profesor = if (profesorIndex != -1) cursor.getString(profesorIndex) else null,
                    nota = if (notaIndex != -1) cursor.getString(notaIndex) else null
                )
                listaAsignaturas.add(asignatura)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return listaAsignaturas
    }

    fun actualizarAsignatura(asignatura: Asignatura): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_NOMBRE, asignatura.nombre)
            put(COL_DIA, asignatura.dia)
            put(COL_HORA_INICIO, asignatura.horaInicio)
            put(COL_HORA_FIN, asignatura.horaFin)
            put(COL_AULA, asignatura.aula)
            put(COL_PROFESOR, asignatura.profesor)
            put(COL_NOTA, asignatura.nota)
        }

        val resultado = db.update(
            TABLA_ASIGNATURAS,
            contentValues,
            "$COL_ID = ?",
            arrayOf(asignatura.id.toString())
        )

        db.close()
        return resultado
    }


}
