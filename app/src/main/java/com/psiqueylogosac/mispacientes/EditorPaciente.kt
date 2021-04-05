package com.psiqueylogosac.mispacientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.annotation.MainThread
import com.google.android.material.switchmaterial.SwitchMaterial
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
            Thread {
                uid = this.intent.getStringExtra("uid")
                val paciente = baseDatos.pacienteDao().porUid(uid)

                runOnUiThread {
                    apellidosEt.setText(paciente.apellidos)
                    nombresEt.setText(paciente.nombres)
                    cedulaEt.setText(paciente.cedula)
                    fechaNacimientoEt.setText(paciente.fechaNacimiento)
                    celularEt.setText(paciente.celular)
                    correoElectronicoEt.setText(paciente.correoElectronico)
                    anamnesisEt.setText(paciente.anamnesis)
                    notasEt.setText(paciente.notas)
                    sexo.isChecked = paciente.sexo!! == "H"
                }
            }.start()
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
            runOnUiThread {
                finish()
            }
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

        val paciente = Paciente(
            uid,
            apellidosEt.text.toString(),
            nombresEt.text.toString(),
            s,
            cedulaEt.text.toString(),
            fechaNacimientoEt.text.toString(),
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


}