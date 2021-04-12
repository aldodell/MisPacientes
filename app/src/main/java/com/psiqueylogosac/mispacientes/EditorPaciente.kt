package com.psiqueylogosac.mispacientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditorPaciente : AppCompatActivity() {

    lateinit var nombresEt: EditText
    lateinit var apellidosEt: EditText
    lateinit var cedulaEt: EditText
    lateinit var correoElectronicoEt: EditText
    lateinit var celularEt: EditText
    lateinit var fechaNacimientoEt: EditText
    lateinit var anamnesisEt: EditText
    lateinit var notasEt: EditText
    lateinit var sexo: SwitchMaterial
    lateinit var salvar: Button
    lateinit var descartar: Button

    var uid = ""
    var modo = ""

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_paciente)


        //Creamos los objetos
        nombresEt = findViewById(R.id.nombresEt)
        apellidosEt = findViewById(R.id.apellidosEt)
        cedulaEt = findViewById(R.id.cedulaEt)
        correoElectronicoEt = findViewById(R.id.correoElectronicoEt)
        celularEt = findViewById(R.id.celularEt)
        fechaNacimientoEt = findViewById(R.id.fechaNacimientoEt)
        anamnesisEt = findViewById(R.id.anamnesisEt)
        notasEt = findViewById(R.id.notasEt)
        sexo = findViewById(R.id.sexoSw)
        salvar = findViewById(R.id.salvarBt)
        descartar = findViewById(R.id.descartarBt)


        //Verificamos el modo
        //Cargamos los datos del paciente si venimos del modo "editar"
        modo = this.intent.getStringExtra("modo")!!


        //Precargamos la interfaz si el modo es editar
        if (modo == MODOS.EDITAR.name) {

            uid = this.intent.getStringExtra("uid")
/*
            Thread {
                val paciente = baseDatos.pacienteDao().porUid(uid)

                runOnUiThread {
                    apellidosEt.setText(paciente.apellidos)
                    nombresEt.setText(paciente.nombres)
                    cedulaEt.setText(paciente.cedula)
                    fechaNacimientoEt.setText(formateadorFecha.format(paciente.fechaNacimiento!!))
                    celularEt.setText(paciente.celular)
                    correoElectronicoEt.setText(paciente.correoElectronico)
                    anamnesisEt.setText(paciente.anamnesis)
                    notasEt.setText(paciente.notas)
                    sexo.isChecked = paciente.sexo!! == "H"
                }
            }.start()
            */

        }


//Cambio de etiqueta de sexo segun click al switch
        sexo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sexo.text = getString(R.string.hombre)
            } else {
                sexo.text = getString(R.string.mujer)
            }

        }

        salvar.setOnClickListener {
            salvarDatos()
        }

        descartar.setOnClickListener {

            AlertDialog.Builder(it.context)
                .setTitle(R.string.desea_descartar)
                .setPositiveButton(R.string.si) { d, p ->
                    runOnUiThread {
                        finish()
                    }
                }
                .setNegativeButton(R.string.no) { d, p ->
                    d.dismiss()
                }
                .show()
        }

    }


    fun salvarDatos() {
        var s = ""
        if (sexo.isChecked) {
            s = "H"
        } else {
            s = "M"
        }

        if (modo == MODOS.CREAR.name) {
            uid = UUID.randomUUID().toString()
        }


        val pm = PacienteModelo().apply {
            apellidos = apellidosEt.text.toString()
            nombres = nombresEt.text.toString()
            sexo = s
            cedula = cedulaEt.text.toString()
            fechaNacimiento =
                formateadorFecha.parse(fechaNacimientoEt.text.toString().replace("/", "-"))
            anamnesis = anamnesisEt.text.toString()
            notas = notasEt.text.toString()
            celular = celularEt.text.toString()
            correoElectronico = correoElectronicoEt.text.toString()

        }

        pm.uid = uid


        /* Salvar datos con Firestore */
        db
            .collection("usuarios")
            .document(usuarioId)
            .collection("pacientes")
            .document(uid)
            .set(pm.toHashMap())
            .addOnSuccessListener { finish() }
            .addOnFailureListener { ex ->
                Toast.makeText(this.baseContext, "Error: " + ex.message, Toast.LENGTH_LONG).show()
            }


        /*
          pacienteHashMap(
                    apellidosEt.text.toString(),
                    nombresEt.text.toString(),
                    s,
                    cedulaEt.text.toString(),
                    formateadorFecha.parse(fechaNacimientoEt.text.toString().replace("/", "-")),
                    anamnesisEt.text.toS  cedulaEt.text.toString(tring(),
                    notasEt.text.toString(),
                    celularEt.text.toString(),
                    correoElectronicoEt.text.toString()
                )
         */


/* Salvar datos en ROOM
        val paciente = Paciente(
            uid,
            apellidosEt.text.toString(),
            nombresEt.text.toString(),
            s,
            cedulaEt.text.toString(),
            formateadorFecha.parse(fechaNacimientoEt.text.toString().replace("/", "-")),
            anamnesisEt.text.toString(),
            notasEt.text.toString(),
            celularEt.text.toString(),
            correoElectronicoEt.text.toString()

        )

        Thread {
            if (modo == MODOS.CREAR.name) {
                baseDatos.pacienteDao().insertar(paciente)
            } else if (modo == MODOS.EDITAR.name) {
                baseDatos.pacienteDao().actualizar(paciente)
            }

            runOnUiThread {
                finish()
            }
        }.start()
    }

 */
    }
}