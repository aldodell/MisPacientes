package com.psiqueylogosac.mispacientes

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PacienteAdaptador(var listaPacientesActivity: ListaPacientesActivity) :
    RecyclerView.Adapter<PacienteAdaptador.PacienteViewHolder>() {
    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pacienteTv: TextView = itemView.findViewById(R.id.paciente_tv)
        val borrarIb: ImageButton = itemView.findViewById(R.id.borrarPacienteIb)
        val editarIb: ImageButton = itemView.findViewById(R.id.editarPacienteIb)
        val pacienteFilaAbrirCitaIb =
            itemView.findViewById<ImageButton>(R.id.pacienteFilaAbrirCitaIb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val vh = LayoutInflater.from(parent.context)
            .inflate(R.layout.paciente_fila, parent, false)
        return PacienteViewHolder(vh)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val p = pacientes[position]
        val s = "${p.apellidos} ${p.nombres}"
        holder.pacienteTv.text = s

        holder.editarIb.setOnClickListener {
            val intent = Intent(it.context, EditorPaciente::class.java)
            intent.putExtra("modo", MODOS.EDITAR.name)
            intent.putExtra("uid", p.uid)
            it.context.startActivity(intent)
        }

        holder.borrarIb.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setPositiveButton(
                    it.context.getString(R.string.si)
                ) { dialog, p1 ->
                    Thread {
                        //Eliminamos al paciente y su citas
                        baseDatos.pacienteDao().eliminar(p)
                        baseDatos.citaDao().eliminarCitasDePaciente(p.uid)
                    }
                        .start()
                    listaPacientesActivity.actualizarUI()
                    dialog?.dismiss()

                }
                .setNegativeButton(
                    it.context.getString(R.string.no)
                ) { dialog, p1 -> dialog?.dismiss() }
                .setTitle(R.string.desea_borrar_registro)
                .show()
        }

        holder.pacienteTv.setOnClickListener {
            abrirCitas(it.context, p)
        }

        holder.pacienteFilaAbrirCitaIb.setOnClickListener {
            abrirCitas(it.context, p)
        }

    }


    override fun getItemCount(): Int {
        return pacientes.size
    }

    fun abrirCitas(context: Context, p: Paciente) {
        val intento =
            Intent(context, ListaCitasActivity::class.java)
        intento.putExtra("modo", MODOS.UNO.name)
        intento.putExtra("pacienteUid", p.uid)
        context.startActivity(intento)
    }
}