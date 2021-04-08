package com.psiqueylogosac.mispacientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaCitasActivity : AppCompatActivity() {


    var citas = ArrayList<Cita>()

    lateinit var fab: FloatingActionButton
    lateinit var rv: RecyclerView
    lateinit var adaptador: CitaAdaptador
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
        adaptador = CitaAdaptador(this, pacienteUid!!)

        //Configurar RV
        rv.adapter = adaptador
        rv.layoutManager = LinearLayoutManager(this)

        //Configurar fab
        fab.setOnClickListener {
            val intento = Intent(it.context, EditorCitaActivity::class.java)
            intento.putExtra("modo", MODOS.CREAR.name)
            intento.putExtra("pacienteUid", pacienteUid)
            startActivity(intento)
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