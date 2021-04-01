package com.psiqueylogosac.mispacientes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PacienteAdaptador : RecyclerView.Adapter<PacienteAdaptador.PacienteViewHolder>() {
    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pacienteTv: TextView = itemView.findViewById(R.id.paciente_tv)
        val borrarIb : ImageButton = itemView.findViewById(R.id.borrarPacienteIb)
        val editarIb : ImageButton = itemView.findViewById(R.id.editarPacienteIb)
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

        holder.editarIb.setOnClickListener {
            val intent = Intent(it.context, EditorPaciente::class.java)
            intent.putExtra("modo","editar")
            intent.putExtra("uid", p.uid)
            it.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return pacientes.size
    }
}