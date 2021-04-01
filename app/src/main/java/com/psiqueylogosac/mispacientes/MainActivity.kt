package com.psiqueylogosac.mispacientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton


lateinit var baseDatos : AppBaseDatos
var pacientes = ArrayList<Paciente>()


class MainActivity : AppCompatActivity() {

    lateinit var pacientes_rv : RecyclerView
    lateinit var fab : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        baseDatos = Room.databaseBuilder(applicationContext,AppBaseDatos::class.java,"pacientes").build()


        fab = findViewById(R.id.fab)



        //Asignamos la repsuesta al boton agregar paciente
        fab.setOnClickListener {
            var intent = Intent(it.context, EditorPaciente::class.java)
            startActivity(intent)
        }

    }
}