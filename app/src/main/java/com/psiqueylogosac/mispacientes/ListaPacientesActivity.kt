package com.psiqueylogosac.mispacientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ListaPacientesActivity : AppCompatActivity() {
    private var usuarioId : String?=null

    lateinit var pacientes_rv: RecyclerView
    lateinit var fab: FloatingActionButton


    lateinit var adaptadorFs: AdaptadorPacienteFs
    lateinit var botonAyuda: ImageButton
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_pacientes)
        usuarioId = Preferencias(this).usuarioId

        //Instancia del adaptador del RV
        val query = db.collection("usuarios")
            .document(usuarioId!!)
            .collection("pacientes")
            .whereEqualTo("activo",true)
            .orderBy("apellidos")
            .orderBy("nombres")

        val options = FirestoreRecyclerOptions.Builder<PacienteModelo>()
            .setQuery(query, PacienteModelo::class.java).build()
        adaptadorFs = AdaptadorPacienteFs(this, options)//PacienteAdaptador(this)

        //Instancia del botón para agregar pacientes
        fab = findViewById(R.id.fab)

        //Instancia del RV
        pacientes_rv = findViewById(R.id.recyclerView)

        //Boton ayuda
        botonAyuda = findViewById(R.id.listaPacientesAyudaIb)



        //Configuración del RV
        pacientes_rv.adapter = adaptadorFs
        pacientes_rv.layoutManager = LinearLayoutManager(applicationContext)

        //Asignamos la repsuesta al boton agregar paciente
        fab.setOnClickListener {
            val intent = Intent(it.context, EditorPaciente::class.java)
            intent.putExtra("modo", MODOS.CREAR.name)
            startActivity(intent)
        }

        //Mostramos la ayuda
        botonAyuda.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setMessage(R.string.intrucciones_administrar_pacientes)
                .setNeutralButton(R.string.si) { dialogo, _ ->
                    dialogo.dismiss()
                }
                .show()
        }
        actualizarUI()
    }


    fun actualizarUI() {
        Log.i("aldox", "ON START")

        adaptadorFs.notifyDataSetChanged()


    }

    override fun onResume() {
        super.onResume()
        actualizarUI()
    }

    override fun onStart() {
        super.onStart()
        adaptadorFs.startListening()
    }

    override fun onStop() {
        super.onStop()
        adaptadorFs.stopListening()
    }

}