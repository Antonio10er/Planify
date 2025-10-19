package com.espinoza.planify

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.espinoza.planify.adapters.DiaSemanaAdapter
import com.espinoza.planify.db.DatabaseHelper
import com.espinoza.planify.models.Asignatura
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HorarioActivity : AppCompatActivity() {

    private lateinit var recyclerViewDias: RecyclerView
    private lateinit var adapter: DiaSemanaAdapter
    private lateinit var dbHelper: DatabaseHelper
    private val diasDeLaSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    private val crearAsignaturaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val nuevaAsignatura = Asignatura(
                nombre = data?.getStringExtra("nombre") ?: "",
                dia = data?.getStringExtra("dia") ?: "",
                horaInicio = data?.getStringExtra("horaInicio") ?: "",
                horaFin = data?.getStringExtra("horaFin") ?: "",
                aula = data?.getStringExtra("aula"),
                profesor = data?.getStringExtra("profesor"),
                nota = data?.getStringExtra("nota")
            )
            dbHelper.insertarAsignatura(nuevaAsignatura)
            Toast.makeText(this, "Asignatura guardada", Toast.LENGTH_SHORT).show()
        }
    }

    private val editarAsignaturaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val asignaturaActualizada = Asignatura(
                id = data?.getIntExtra("id", -1) ?: -1, // <-- MUY IMPORTANTE
                nombre = data?.getStringExtra("nombre") ?: "",
                dia = data?.getStringExtra("dia") ?: "",
                horaInicio = data?.getStringExtra("horaInicio") ?: "",
                horaFin = data?.getStringExtra("horaFin") ?: "",
                aula = data?.getStringExtra("aula"),
                profesor = data?.getStringExtra("profesor"),
                nota = data?.getStringExtra("nota")
            )

            if (asignaturaActualizada.id != -1) {
                dbHelper.actualizarAsignatura(asignaturaActualizada)
                Toast.makeText(this, "Asignatura actualizada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horario)

        dbHelper = DatabaseHelper(this)

        findViewById<FloatingActionButton>(R.id.fabAgregarAsignatura).setOnClickListener {
            val intent = Intent(this, FormularioAsignaturaActivity::class.java)
            crearAsignaturaLauncher.launch(intent)
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        recyclerViewDias = findViewById(R.id.recyclerViewDias)
        recyclerViewDias.layoutManager = LinearLayoutManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarVista()
    }

    private fun actualizarVista() {
        val listaDeAsignaturas = dbHelper.obtenerTodasLasAsignaturas()

        adapter = DiaSemanaAdapter(
            diasDeLaSemana,
            listaDeAsignaturas,
            onEditarClick = { asignaturaAEditar ->
                abrirFormularioParaEditar(asignaturaAEditar)
            },
            onEliminarClick = { asignaturaAEliminar ->
                mostrarDialogoConfirmacion(asignaturaAEliminar)
            }
        )
        recyclerViewDias.adapter = adapter
    }

    private fun abrirFormularioParaEditar(asignatura: Asignatura) {
        val intent = Intent(this, FormularioAsignaturaActivity::class.java).apply {
            putExtra("MODO", "EDITAR")
            putExtra("id", asignatura.id)
            putExtra("nombre", asignatura.nombre)
            putExtra("dia", asignatura.dia)
            putExtra("horaInicio", asignatura.horaInicio)
            putExtra("horaFin", asignatura.horaFin)
            putExtra("aula", asignatura.aula)
            putExtra("profesor", asignatura.profesor)
            putExtra("nota", asignatura.nota)
        }
        editarAsignaturaLauncher.launch(intent)
    }

    private fun mostrarDialogoConfirmacion(asignatura: Asignatura) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar '${asignatura.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                dbHelper.eliminarAsignatura(asignatura.id)
                actualizarVista()
                Toast.makeText(this, "Asignatura eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
