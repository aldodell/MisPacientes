package com.psiqueylogosac.mispacientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaPacientesActivity : AppCompatActivity() {


    lateinit var pacientes_rv: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var adaptador: PacienteAdaptador


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_pacientes)


        //Instancia del adaptador del RV
        adaptador = PacienteAdaptador(this)

        //Instancia del botón para agregar pacientes
        fab = findViewById(R.id.fab)

        //Instancia del RV
        pacientes_rv = findViewById(R.id.recyclerView)

        //Instancia singleton de la base de datos
        baseDatos =
            Room.databaseBuilder(applicationContext, AppBaseDatos::class.java, "pacientes")
                .allowMainThreadQueries()
                .build()

        //Configuración del RV
        pacientes_rv.adapter = adaptador
        pacientes_rv.layoutManager = LinearLayoutManager(applicationContext)

        //Asignamos la repsuesta al boton agregar paciente
        fab.setOnClickListener {
            val intent = Intent(it.context, EditorPaciente::class.java)
            intent.putExtra("modo", MODOS.CREAR.name)
            startActivity(intent)
        }


    }


    fun actualizarUI() {
        Log.i("aldox", "ON START")

        //Leemos la base de datos:
        Thread {
            pacientes.clear()
            pacientes.addAll(
                baseDatos.pacienteDao().todos() as ArrayList<Paciente>
            )
            runOnUiThread {
                pacientes_rv.adapter?.notifyDataSetChanged()
                pacientes_rv.invalidate()
                Log.i("aldox", "objetos: ${pacientes.size}")
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        actualizarUI()
    }

}