package com.psiqueylogosac.mispacientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ListaCitasActivity : AppCompatActivity() {


    lateinit var fab: FloatingActionButton
    lateinit var rv: RecyclerView
    var adaptador: CitaAdaptador? = null
    lateinit var botonAyuda: ImageButton
    lateinit var modo: MODOS
    var pacienteUid: String? = null
    lateinit var db: FirebaseFirestore

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


        db = FirebaseFirestore.getInstance()


        //Configuramos el adaptador
        val query = db.collection("usuarios/$usuarioId/citas")
            .whereEqualTo("pacienteUid", pacienteUid)

        val options = FirestoreRecyclerOptions.Builder<CitaModelo>()
            .setQuery(query, CitaModelo::class.java)
            .build()


        //Cargamos el paciente actual y lo enviamos al adaptador
        db.document("usuarios/$usuarioId/pacientes/$pacienteUid")
            .get()
            .addOnSuccessListener {

                val paciente = PacienteModelo()
                paciente.fromHashMap(it.data)

                //Configuramos el adaptador
                adaptador = CitaAdaptador(this, paciente, options)

                //Configurar RV
                rv.adapter = adaptador
                rv.layoutManager = LinearLayoutManager(this)
                adaptador!!.startListening()

            }
            .addOnFailureListener {
                Toast.makeText(
                    this.baseContext,
                    "ERROR RECUPERANDO PACIENTE: ${it.message!!}",
                    Toast.LENGTH_LONG
                ).show()
            }


        //Configurar fab
        fab.setOnClickListener {
            val intento = Intent(it.context, EditorCitaActivity::class.java)
            intento.putExtra("modo", MODOS.CREAR.name)
            intento.putExtra("pacienteUid", pacienteUid)
            startActivity(intento)
        }

        //Configuramos la ayuda:
        botonAyuda.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setMessage(getString(R.string.seleccionar_agregar_cita))
                .setNeutralButton(R.string.si) { dialogo, _ ->
                    dialogo.dismiss()
                }.show()
        }

    }


    fun actualizarUI() {
        adaptador?.notifyDataSetChanged()

    }

    override fun onStart() {
        super.onStart()
        if (adaptador != null) {
            adaptador!!.startListening()
        }

    }

    override fun onStop() {
        super.onStop()
        if (adaptador != null) {
            adaptador?.stopListening()
        }
    }
}