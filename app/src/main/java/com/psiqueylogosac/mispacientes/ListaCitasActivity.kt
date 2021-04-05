package com.psiqueylogosac.mispacientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaCitasActivity : AppCompatActivity() {


    var citas = ArrayList<Cita>()
    lateinit var fab: FloatingActionButton
    lateinit var rv: RecyclerView
    lateinit var adaptador: CitaAdaptador
    lateinit var modo: MODOS
    var pacienteUid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_citas)


        //Detectamos el modo
        modo = MODOS.valueOf(
            intent.getStringExtra("modo")!!
        )

        //Obtenemos el uid paciente si lo hay
        pacienteUid = intent.getStringExtra("pacienteUid")

        fab = findViewById(R.id.listaCitaFab)
        rv = findViewById(R.id.listaCitaRv)
        adaptador = CitaAdaptador(this)


        //Configurar RV
        rv.adapter = adaptador
        rv.layoutManager = LinearLayoutManager(this)

        //Configurar fab
        fab.setOnClickListener {
            val intento = Intent(it.context, EditorCitaActivity::class.java)
            intento.putExtra("modo", MODOS.CREAR.name)
            startActivity(intento)
        }

        //Actualizamos el RV
        actualizarUI()

    }

    fun actualizarUI(pacienteUid: String? = null) {
        Thread {
            citas.clear()

            when (modo) {
                MODOS.UNO -> {
                    citas = baseDatos.citaDato().porPaciente(pacienteUid!!) as ArrayList<Cita>
                }
                MODOS.TODOS -> {
                    citas = baseDatos.citaDato().todos() as ArrayList<Cita>
                }
                else ->{}
            }
            runOnUiThread {
                adaptador.notifyDataSetChanged()
            }
        }
    }
}