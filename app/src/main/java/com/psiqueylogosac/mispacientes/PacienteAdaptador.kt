package com.psiqueylogosac.mispacientes

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PacienteAdaptador(var mainActivity: MainActivity) :
    RecyclerView.Adapter<PacienteAdaptador.PacienteViewHolder>() {
    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pacienteTv: TextView = itemView.findViewById(R.id.paciente_tv)
        val borrarIb: ImageButton = itemView.findViewById(R.id.borrarPacienteIb)
        val editarIb: ImageButton = itemView.findViewById(R.id.editarPacienteIb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        var vh = LayoutInflater.from(parent.context)
            .inflate(R.layout.paciente_fila, parent, false)
        return PacienteViewHolder(vh)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val p = pacientes[position]
        val s = "${p.apellidos} ${p.nombres}"
        holder.pacienteTv.text = s

        holder.editarIb.setOnClickListener {
            val intent = Intent(it.context, EditorPaciente::class.java)
            intent.putExtra("modo", MODO_EDITAR)
            intent.putExtra("uid", p.uid)
            it.context.startActivity(intent)
        }

        holder.borrarIb.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setPositiveButton(
                    it.context.getString(R.string.si)
                ) { dialog, p1 ->
                    val hilo = Thread {
                        baseDatos.pacienteDao().eliminar(p)
                    }
                    hilo.start()
                    hilo.join()
                    mainActivity.actualizarUI()
                    dialog?.dismiss()

                }
                .setNegativeButton(
                    it.context.getString(R.string.no)
                ) { dialog, p1 -> dialog?.dismiss() }
                .setTitle(R.string.desea_borrar_registro)
                .show()
        }

    }

    override fun getItemCount(): Int {
        return pacientes.size
    }
}