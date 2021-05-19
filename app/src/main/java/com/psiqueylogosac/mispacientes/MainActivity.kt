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
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


//lateinit var baseDatos: AppBaseDatos

var pacientes = ArrayList<PacienteModelo>()
val formateadorFecha = SimpleDateFormat("dd-MM-yyyy")
val formateadorHora = SimpleDateFormat("hh:mm a")
val formateadorFechaHora = SimpleDateFormat("dd-MM-yyyy hh:mm a")

var usuario: FirebaseUser? = null
var usuarioId : String ? = null

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
    var adaptador: MainCitaAdaptador? = null
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Obtenemos las vistas
        mainAdministrarPacientesIb = findViewById(R.id.mainAdministrarPacientesIb)
        mainProximasCitasRv = findViewById(R.id.mainProximasCitasRv)
        mainAjustesBoton = findViewById(R.id.mainAjustesBt)



        // Buscamos en las preferencias si está registrado el usuario
        usuarioId = Preferencias(this).usuarioId

        if(usuarioId!=null) {


            //Configuramos el RV y su adaptador
            val query = db.collection("usuarios/$usuarioId/citas")
                .whereGreaterThanOrEqualTo("fechaHora", Date())

            val opciones = FirestoreRecyclerOptions.Builder<CitaModelo>()
                .setQuery(query, CitaModelo::class.java)
                .build()

            adaptador = MainCitaAdaptador(this, opciones)

            mainProximasCitasRv.apply {
                adapter = adaptador!!
                layoutManager = LinearLayoutManager(this.context)
            }


        } else {
            AlertDialog.Builder(this)
                .setMessage(R.string.es_necesario_registrarse)
                .setNeutralButton(R.string.ok) { d, _ ->
                    val intento = Intent(this.baseContext, AjustesActivity::class.java)
                    startActivity(intento)
                }.show()
        }


        //Configuramos el botón de administrar pacients
        mainAdministrarPacientesIb.setOnClickListener {
            val intento = Intent(this, ListaPacientesActivity::class.java)
            startActivity(intento)
        }


        //Configuramos el botón de ajustes
        mainAjustesBoton.setOnClickListener {
            val intento = Intent(it.context, AjustesActivity::class.java)
            startActivity(intento)
        }


    }


    override fun onStart() {
        super.onStart()
        if (adaptador != null) {
            adaptador?.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (adaptador != null) {
            adaptador?.stopListening()
        }
    }


}

