package com.psiqueylogosac.mispacientes

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


lateinit var baseDatos: AppBaseDatos

var pacientes = ArrayList<Paciente>()
val formateadorFecha = SimpleDateFormat("dd-MM-yyyy")
val formateadorHora = SimpleDateFormat("hh:mm a")
var usuario: FirebaseUser? = null
var usuarioId = ""

enum class MODOS(val m: String) {
    CREAR("crear"),
    EDITAR("editar"),
    TODOS("todos"),
    UNO("uno")
}


class MainActivity : AppCompatActivity() {

    lateinit var mainAdministrarPacientesIb: ImageButton
    lateinit var mainProximasCitasRv: RecyclerView
    lateinit var mainAjustesBoton: ImageButton

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

        /*
        Buscamos en las preferencias si está registrado el usuario
         */
        val prefs: SharedPreferences = getSharedPreferences("ajustes", MODE_PRIVATE)
        val u = prefs.getString("usuarioId", null)
        if (u != null) {
            usuarioId = u
        } else {
            AlertDialog.Builder(this)
                .setMessage(R.string.es_necesario_registrarse)
                .setNeutralButton(R.string.ok) { d, _ ->
                    val intento = Intent(this.baseContext, AjustesActivity::class.java)
                    startActivity(intento)
                    d.dismiss()
                }.show()
        }
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

