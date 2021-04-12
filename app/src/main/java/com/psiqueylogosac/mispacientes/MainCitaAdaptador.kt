package com.psiqueylogosac.mispacientes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class MainCitaAdaptador(mainActivity: MainActivity, options: FirestoreRecyclerOptions<CitaModelo>) :
    FirestoreRecyclerAdapter<CitaModelo, MainCitaAdaptador.ViewHolder>(
        options
    ) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCitaFilaLinea1Tv = itemView.findViewById<TextView>(R.id.mainCitaFilaLinea1Tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val fila =
            LayoutInflater.from(parent.context).inflate(R.layout.main_cita_fila, parent, false)
        return ViewHolder(fila)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: CitaModelo) {

        val s = "${formateadorFechaHora.format(model.fechaHora!!)} ${model.paciente?.apellidos} ${model.paciente?.nombres}"
        holder.mainCitaFilaLinea1Tv.text = s


    }
}


/*
class MainCitaAdaptador(var mainActivity: MainActivity) :
    RecyclerView.Adapter<MainCitaAdaptador.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCitaFilaLinea1Tv = itemView.findViewById<TextView>(R.id.mainCitaFilaLinea1Tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val fila =
            LayoutInflater.from(parent.context).inflate(R.layout.main_cita_fila, parent, false)
        return ViewHolder(fila)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val c = mainActivity.proximasCitas[position]

        val s = "${formateadorFecha.format(c.cita.fecha!!)} ${formateadorHora.format(c.cita.hora!!)} ${c.paciente.apellidos} ${c.paciente.nombres}"
        holder.mainCitaFilaLinea1Tv.text = s
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return mainActivity.proximasCitas.size
    }
}

*/