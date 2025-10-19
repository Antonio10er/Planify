package com.espinoza.planify

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class FormularioAsignaturaActivity : AppCompatActivity() {

    private var diaSeleccionado: String? = null
    private var horaInicioSeleccionada: String? = null
    private var horaFinSeleccionada: String? = null
    private var modoEdicion = false
    private var idAsignaturaActual = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_asignatura)

        val btnBack: ImageButton = findViewById(R.id.btnFormularioBack)
        val btnGuardar: Button = findViewById(R.id.btnGuardar)
        val btnEstablecerDia: Button = findViewById(R.id.btnEstablecerDia)
        val btnHoraInicio: Button = findViewById(R.id.btnHoraInicio)
        val btnHoraFin: Button = findViewById(R.id.btnHoraFin)
        val etAsignatura: EditText = findViewById(R.id.etAsignatura)
        val etAula: EditText = findViewById(R.id.etAula)
        val etProfesor: EditText = findViewById(R.id.etProfesor)
        val etNota: EditText = findViewById(R.id.etNota)

        if (intent.getStringExtra("MODO") == "EDITAR") {
            modoEdicion = true
            idAsignaturaActual = intent.getIntExtra("id", -1)

            etAsignatura.setText(intent.getStringExtra("nombre"))
            etAula.setText(intent.getStringExtra("aula"))
            etProfesor.setText(intent.getStringExtra("profesor"))
            etNota.setText(intent.getStringExtra("nota"))

            diaSeleccionado = intent.getStringExtra("dia")
            btnEstablecerDia.text = diaSeleccionado

            horaInicioSeleccionada = intent.getStringExtra("horaInicio")
            btnHoraInicio.text = horaInicioSeleccionada

            horaFinSeleccionada = intent.getStringExtra("horaFin")
            btnHoraFin.text = horaFinSeleccionada

            btnGuardar.text = "Actualizar"
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnEstablecerDia.setOnClickListener {
            mostrarDialogoDia()
        }

        btnHoraInicio.setOnClickListener {
            mostrarDialogoHora(esHoraInicio = true)
        }

        btnHoraFin.setOnClickListener {
            mostrarDialogoHora(esHoraInicio = false)
        }

        btnGuardar.setOnClickListener {
            val nombreAsignatura = etAsignatura.text.toString()
            val aula = etAula.text.toString()
            val profesor = etProfesor.text.toString()
            val nota = etNota.text.toString()

            if (nombreAsignatura.isBlank() || diaSeleccionado == null || horaInicioSeleccionada == null || horaFinSeleccionada == null) {
                Toast.makeText(this, "Completa Asignatura, Día y Hora", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intentDeResultado = Intent()
            intentDeResultado.putExtra("nombre", nombreAsignatura)
            intentDeResultado.putExtra("dia", diaSeleccionado)
            intentDeResultado.putExtra("horaInicio", horaInicioSeleccionada)
            intentDeResultado.putExtra("horaFin", horaFinSeleccionada)
            intentDeResultado.putExtra("aula", aula)
            intentDeResultado.putExtra("profesor", profesor)
            intentDeResultado.putExtra("nota", nota)

            if (modoEdicion) {
                intentDeResultado.putExtra("id", idAsignaturaActual)
            }

            setResult(Activity.RESULT_OK, intentDeResultado)
            finish()
        }
    }

    private fun mostrarDialogoDia() {
        val dias = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        AlertDialog.Builder(this)
            .setTitle("Selecciona un día")
            .setItems(dias) { _, which ->
                diaSeleccionado = dias[which]
                findViewById<Button>(R.id.btnEstablecerDia).text = diaSeleccionado
            }
            .show()
    }

    private fun mostrarDialogoHora(esHoraInicio: Boolean) {
        val calendario = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val horaFormateada = String.format("%02d:%02d", hourOfDay, minute)
                if (esHoraInicio) {
                    horaInicioSeleccionada = horaFormateada
                    findViewById<Button>(R.id.btnHoraInicio).text = horaFormateada
                } else {
                    horaFinSeleccionada = horaFormateada
                    findViewById<Button>(R.id.btnHoraFin).text = horaFormateada
                }
            },
            calendario.get(Calendar.HOUR_OF_DAY),
            calendario.get(Calendar.MINUTE),
            true
        ).show()
    }
}
