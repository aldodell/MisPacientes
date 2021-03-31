package com.psiqueylogosac.mispacientes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PacienteAdaptador : RecyclerView.Adapter<PacienteAdaptador.PacienteViewHolder>() {
    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pacienteTv: TextView = itemView.findViewById(R.id.paciente_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        var vh = LayoutInflater.from(parent.context)
            .inflate(R.layout.paciente_fila,parent,false)
        return PacienteViewHolder(vh)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val p = pacientes[position]
        val s = "${p.apellidos} ${p.nombres}"
        holder.pacienteTv.text = s
    }

    override fun getItemCount(): Int {
        return pacientes.size
    }
}