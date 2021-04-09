package com.psiqueylogosac.mispacientes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class CitaAdaptador(var listaCitasActivity: ListaCitasActivity, var pacienteUid: String) :
    RecyclerView.Adapter<CitaAdaptador.CitaViewHolder>() {

    var paciente: Paciente = baseDatos.pacienteDao().porUid(pacienteUid)

    class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fechaHoraTv = itemView.findViewById<TextView>(R.id.listaCitaFechaHoraTv)
        val nombresApellidos = itemView.findViewById<TextView>(R.id.listaCitaApellidosNombresTv)
        val editarIb = itemView.findViewById<ImageButton>(R.id.listaCitaEditarIb)
        val eliminarIb = itemView.findViewById<ImageButton>(R.id.listaCitaEliminarIb)

    }

    /**
     * Called when RecyclerView needs a new [CitaViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val fila =
            LayoutInflater.from(parent.context).inflate(R.layout.lista_cita_fila, parent, false)
        return CitaViewHolder(fila)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [CitaViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [CitaViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = listaCitasActivity.citas[position]
        val fechaHora =
            formateadorFecha.format(cita.fecha!!) + " " + formateadorHora.format(cita.hora!!)
        val apellidosNombres = "${paciente.apellidos} ${paciente.nombres}"

        holder.fechaHoraTv.setText(fechaHora)
        holder.nombresApellidos.setText(apellidosNombres)



        //Boton modificar
        holder.editarIb.setOnClickListener {
            val intento = Intent(it.context, EditorCitaActivity::class.java)
            intento.putExtra("modo", MODOS.EDITAR.name)
            intento.putExtra("citaUid", cita.uid)
            intento.putExtra("pacienteUid",pacienteUid)
            it.context.startActivity(intento)
        }


        //boton eliminar
        holder.eliminarIb.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setTitle(R.string.desea_borrar_registro)
                .setPositiveButton(R.string.si) { dialog, p1 ->
                    Thread {
                        baseDatos.citaDao().eliminar(cita)
                        listaCitasActivity.runOnUiThread {
                            listaCitasActivity.actualizarUI()
                        }
                    }.start()
                    listaCitasActivity.actualizarUI()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, p1 ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return listaCitasActivity.citas.size
    }

}