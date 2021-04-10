package com.psiqueylogosac.mispacientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


lateinit var baseDatos: AppBaseDatos

var pacientes = ArrayList<Paciente>()
val formateadorFecha = SimpleDateFormat("dd-MM-yyyy")
val formateadorHora = SimpleDateFormat("hh:mm a")
val RC_SIGN_IN = 1
var usuario : FirebaseUser? = null

enum class MODOS(val m: String) {
    CREAR("crear"),
    EDITAR("editar"),
    TODOS("todos"),
    UNO("uno")
}


class MainActivity : AppCompatActivity() {

    lateinit var mainAdministrarPacientesIb: ImageButton
    lateinit var mainProximasCitasRv: RecyclerView
    lateinit var mainAjustesBoton : ImageButton

    var proximasCitas = ArrayList<CitasConPaciente>()
    var adaptador = MainCitaAdaptador(this)
    private lateinit var autorizador: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        baseDatos = Room.databaseBuilder(this, AppBaseDatos::class.java, "pacientes")
            .build()


        mainAdministrarPacientesIb = findViewById(R.id.mainAdministrarPacientesIb)
        mainProximasCitasRv = findViewById(R.id.mainProximasCitasRv)
        mainAjustesBoton = findViewById(R.id.mainAjustesBt)

        mainAdministrarPacientesIb.setOnClickListener {
            val intento = Intent(this, ListaPacientesActivity::class.java)
            startActivity(intento)
        }

        mainProximasCitasRv.apply {
            adapter = adaptador
            layoutManager = LinearLayoutManager(this.context)
        }


        mainAjustesBoton.setOnClickListener {
            val intento = Intent(it.context, AjustesActivity::class.java)
            startActivity(intento)
        }


        //actualizarUI()
    }

    fun actualizarUI() {
        proximasCitas.clear()
        Thread {
            proximasCitas.addAll(baseDatos.citaDao().aPartirDe(Date()))
            runOnUiThread {
                adaptador.notifyDataSetChanged()
                /*
                Toast.makeText(
                    this.applicationContext,
                    "citas: ${proximasCitas.size}",
                    Toast.LENGTH_LONG
                ).show()
                */

            }
        }.start()

    }

    override fun onResume() {
        super.onResume()
        actualizarUI()
    }



}

