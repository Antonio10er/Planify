package com.espinoza.planify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgenda = findViewById<Button>(R.id.btnAgenda)
        val btnHorario = findViewById<Button>(R.id.btnHorario)
        val btnAsignaturas = findViewById<Button>(R.id.btnAsignaturas)
        val btnCalendario = findViewById<Button>(R.id.btnCalendario)

        btnAgenda.setOnClickListener {
            val intent = Intent(this, AgendaActivity::class.java)
            startActivity(intent)
        }

        btnHorario.setOnClickListener {
            val intent = Intent(this, HorarioActivity::class.java)
            startActivity(intent)
        }

        btnAsignaturas.setOnClickListener {
            val intent = Intent(this, AsignaturasActivity::class.java)
            startActivity(intent)
        }

        btnCalendario.setOnClickListener {
            val intent = Intent(this, CalendarioActivity::class.java)
            startActivity(intent)
        }

    }


}