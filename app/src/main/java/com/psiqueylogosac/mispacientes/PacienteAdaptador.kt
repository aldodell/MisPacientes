package com.psiqueylogosac.mispacientes

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

/*
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

 */


class PacienteViewHolderFs(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pacienteTv: TextView = itemView.findViewById(R.id.paciente_tv)
    val borrarIb: ImageButton = itemView.findViewById(R.id.borrarPacienteIb)
    val editarIb: ImageButton = itemView.findViewById(R.id.editarPacienteIb)
    val pacienteFilaAbrirCitaIb =
        itemView.findViewById<ImageButton>(R.id.pacienteFilaAbrirCitaIb)
}

class AdaptadorPacienteFs(
    var listaPacientesActivity: ListaPacientesActivity,
    options: FirestoreRecyclerOptions<PacienteModelo>
) : FirestoreRecyclerAdapter<PacienteModelo, PacienteViewHolderFs>(options) {

    val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolderFs {
        val vh = LayoutInflater.from(parent.context)
            .inflate(R.layout.paciente_fila, parent, false)
        return PacienteViewHolderFs(vh)
    }

    /**
     * @param model the model object containing the data that should be used to populate the view.
     * @see .onBindViewHolder
     */
    override fun onBindViewHolder(
        holder: PacienteViewHolderFs,
        position: Int,
        paciente: PacienteModelo
    ) {

        fun abrirCitas(context: Context, p: PacienteModelo) {
            val intento =
                Intent(context, ListaCitasActivity::class.java)
            intento.putExtra("modo", MODOS.UNO.name)
            intento.putExtra("pacienteUid", p.uid)
            context.startActivity(intento)
        }

        val s = "${paciente.apellidos} ${paciente.nombres}"
        holder.pacienteTv.text = s

        holder.editarIb.setOnClickListener {
            val intent = Intent(it.context, EditorPaciente::class.java)
            intent.putExtra("modo", MODOS.EDITAR.name)
            intent.putExtra("uid", paciente.uid)
            it.context.startActivity(intent)
        }

        holder.borrarIb.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setPositiveButton(
                    it.context.getString(R.string.si)
                ) { dialog, p1 ->
                    val path = "usuarios/$usuarioId/pacientes/${paciente.uid}"
                    db.document(path)
                        .update("activo", false)
                        .addOnSuccessListener {
                            listaPacientesActivity.actualizarUI()
                            dialog?.dismiss()
                        }
                }
                .setNegativeButton(
                    it.context.getString(R.string.no)
                ) { dialog, p1 -> dialog?.dismiss() }
                .setTitle(R.string.desea_borrar_registro)
                .show()
        }

        holder.pacienteTv.setOnClickListener {
            abrirCitas(it.context, paciente)
        }

        holder.pacienteFilaAbrirCitaIb.setOnClickListener {
            abrirCitas(it.context, paciente)
        }

    }

    override fun onError(e: FirebaseFirestoreException) {
        super.onError(e)
        Toast.makeText(listaPacientesActivity.baseContext, e.message!!, Toast.LENGTH_LONG).show()
        Log.i("aldox", e.message!!)
    }

}