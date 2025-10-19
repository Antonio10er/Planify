package com.espinoza.planify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espinoza.planify.R
import com.espinoza.planify.models.Asignatura

class DiaSemanaAdapter(
    private val diasDeLaSemana: List<String>,
    private val todasLasAsignaturas: List<Asignatura>,
    private val onEditarClick: (Asignatura) -> Unit,
    private val onEliminarClick: (Asignatura) -> Unit
) : RecyclerView.Adapter<DiaSemanaAdapter.DiaViewHolder>() {

    class DiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreDia: TextView = itemView.findViewById(R.id.textViewNombreDia)
        val contenedorAsignaturas: LinearLayout = itemView.findViewById(R.id.contenedor_asignaturas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dia_semana, parent, false)
        return DiaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diasDeLaSemana.size
    }

    override fun onBindViewHolder(holder: DiaViewHolder, position: Int) {
        val diaActual = diasDeLaSemana[position]
        holder.nombreDia.text = diaActual
        holder.contenedorAsignaturas.removeAllViews()

        val asignaturasParaEsteDia = todasLasAsignaturas.filter { it.dia == diaActual }

        if (asignaturasParaEsteDia.isNotEmpty()) {
            val inflater = LayoutInflater.from(holder.itemView.context)
            for (asignatura in asignaturasParaEsteDia) {
                val vistaAsignatura = inflater.inflate(R.layout.item_asignatura, holder.contenedorAsignaturas, false)

                val tvNombre = vistaAsignatura.findViewById<TextView>(R.id.tvNombreAsignatura)
                val tvDetalles = vistaAsignatura.findViewById<TextView>(R.id.tvDetallesAsignatura)

                tvNombre.text = asignatura.nombre
                val detalles = "${asignatura.horaInicio} - ${asignatura.horaFin}" +
                        (asignatura.aula?.takeIf { it.isNotBlank() }?.let { " | Aula: $it" } ?: "")
                tvDetalles.text = detalles

                val tvProfesor = vistaAsignatura.findViewById<TextView>(R.id.tvProfesorAsignatura)
                val tvNota = vistaAsignatura.findViewById<TextView>(R.id.tvNotaAsignatura)

                asignatura.profesor?.takeIf { it.isNotBlank() }?.let { profesor ->
                    tvProfesor.text = "Prof: $profesor"
                    tvProfesor.visibility = View.VISIBLE
                }

                asignatura.nota?.takeIf { it.isNotBlank() }?.let { nota ->
                    tvNota.text = nota
                    tvNota.visibility = View.VISIBLE
                }

                val btnEliminar = vistaAsignatura.findViewById<ImageButton>(R.id.btnEliminarAsignatura)
                btnEliminar.setOnClickListener {
                    onEliminarClick(asignatura)
                }

                val btnEditar = vistaAsignatura.findViewById<ImageButton>(R.id.btnEditarAsignatura)
                btnEditar.setOnClickListener {
                    onEditarClick(asignatura)
                }

                holder.contenedorAsignaturas.addView(vistaAsignatura)
            }
        }
    }
}
