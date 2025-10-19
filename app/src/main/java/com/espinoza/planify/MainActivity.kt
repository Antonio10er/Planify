package com.espinoza.planify

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.espinoza.planify.adapters.AsignaturaHoyAdapter
import com.espinoza.planify.db.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvTituloHoy: TextView
    private lateinit var recyclerViewHoy: RecyclerView
    private lateinit var tvHoySinClases: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        tvTituloHoy = findViewById(R.id.tvTituloHoy)
        recyclerViewHoy = findViewById(R.id.recyclerViewHoy)
        tvHoySinClases = findViewById(R.id.tvHoySinClases)

        val btnAgenda = findViewById<Button>(R.id.btnAgenda)
        val btnHorario = findViewById<Button>(R.id.btnHorario)
        val btnAsignaturas = findViewById<Button>(R.id.btnAsignaturas)
        val btnCalendario = findViewById<Button>(R.id.btnCalendario)

        btnAgenda.setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }
        btnHorario.setOnClickListener {
            startActivity(Intent(this, HorarioActivity::class.java))
        }
        btnAsignaturas.setOnClickListener {
            startActivity(Intent(this, AsignaturasActivity::class.java))
        }
        btnCalendario.setOnClickListener {
            startActivity(Intent(this, CalendarioActivity::class.java))
        }

        recyclerViewHoy.layoutManager = LinearLayoutManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        cargarAsignaturasDeHoy()
    }

    private fun cargarAsignaturasDeHoy() {
        val calendario = Calendar.getInstance()
        val formatoDia = SimpleDateFormat("EEEE", Locale("es", "ES"))
        var nombreDiaHoy = formatoDia.format(calendario.time).replaceFirstChar { it.uppercase() }

        if (nombreDiaHoy == "Miercoles") nombreDiaHoy = "Miércoles"
        if (nombreDiaHoy == "Sabado") nombreDiaHoy = "Sábado"

        val asignaturasHoy = dbHelper.obtenerAsignaturasDelDia(nombreDiaHoy)

        if (asignaturasHoy.isEmpty()) {
            tvTituloHoy.visibility = View.GONE
            recyclerViewHoy.visibility = View.GONE
            tvHoySinClases.visibility = View.VISIBLE
        } else {
            tvTituloHoy.visibility = View.VISIBLE
            recyclerViewHoy.visibility = View.VISIBLE
            tvHoySinClases.visibility = View.GONE

            val adapter = AsignaturaHoyAdapter(asignaturasHoy)
            recyclerViewHoy.adapter = adapter
        }
    }
}
