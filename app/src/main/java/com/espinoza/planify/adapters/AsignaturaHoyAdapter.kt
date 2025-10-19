package com.espinoza.planify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup    import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espinoza.planify.R
import com.espinoza.planify.models.Asignatura

class AsignaturaHoyAdapter(private val asignaturas: List<Asignatura>) :
    RecyclerView.Adapter<AsignaturaHoyAdapter.AsignaturaHoyViewHolder>() {

    class AsignaturaHoyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreAsignaturaHoy)
        val tvHora: TextView = view.findViewById(R.id.tvHoraAsignaturaHoy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsignaturaHoyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asignatura_hoy, parent, false)
        return AsignaturaHoyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsignaturaHoyViewHolder, position: Int) {
        val asignatura = asignaturas[position]
        holder.tvNombre.text = asignatura.nombre
        holder.tvHora.text = "${asignatura.horaInicio} - ${asignatura.horaFin}"
    }

    override fun getItemCount() = asignaturas.size
}
