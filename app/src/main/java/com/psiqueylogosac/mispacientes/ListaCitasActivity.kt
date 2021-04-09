package com.psiqueylogosac.mispacientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaCitasActivity : AppCompatActivity() {


    var citas = ArrayList<Cita>()
    lateinit var fab: FloatingActionButton
    lateinit var rv: RecyclerView
    lateinit var adaptador: CitaAdaptador
    lateinit var botonAyuda : ImageButton
    lateinit var modo: MODOS
    var pacienteUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_citas)

        //Detectamos el modo
        modo = MODOS.valueOf(
            intent.getStringExtra("modo")!!
        )

        //Obtenemos el uid paciente si lo hay
        pacienteUid = intent.getStringExtra("pacienteUid")

        //Buscamos las vistas
        fab = findViewById(R.id.listaCitaFab)
        rv = findViewById(R.id.listaCitaRv)
        botonAyuda = findViewById(R.id.listaCitasAyudaIb)
        adaptador = CitaAdaptador(this, pacienteUid!!)

        //Configurar RV
        rv.adapter = adaptador
        rv.layoutManager = LinearLayoutManager(this)

        //Configurar fab
        fab.setOnClickListener {
            //savedInstanceState?.putString("pacienteUid", pacienteUid)
            val intento = Intent(it.context, EditorCitaActivity::class.java)
            intento.putExtra("modo", MODOS.CREAR.name)
            intento.putExtra("pacienteUid", pacienteUid)
            startActivity(intento)
        }

        //Configuramos la ayuda:
        botonAyuda.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setMessage(getString(R.string.seleccionar_agregar_cita))
                .setNeutralButton(R.string.si) { dialogo,_ ->
                    dialogo.dismiss()
                }.show()
        }

        //Actualizamos el RV
        actualizarUI()

    }

    override fun onResume() {
        super.onResume()
        actualizarUI()
    }

    fun actualizarUI() {

        Thread {
            citas.clear()

            when (modo) {
                MODOS.UNO -> {
                    citas.addAll(baseDatos.citaDao().porPaciente(pacienteUid!!))

                }
                MODOS.TODOS -> {
                    citas.addAll(baseDatos.citaDao().todos())
                }
                else -> {
                }
            }
            runOnUiThread {
                adaptador.notifyDataSetChanged()
            }
        }.start()
    }
}