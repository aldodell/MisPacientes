package com.psiqueylogosac.mispacientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.switchmaterial.SwitchMaterial

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_paciente)
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


        sexo.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                sexo.text = getString(R.string.hombre)
            } else  {
                sexo.text = getString(R.string.mujer)
            }

        }

        salvar.setOnClickListener {

            var s = ""
            if (sexo.isChecked) {
                s = "H"
            } else {
                s = "M"
            }
            var paciente = Paciente(
                0,
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
                baseDatos.pacienteDao().insertar(paciente)
                finish()
            }.join()


        }

        descartar.setOnClickListener {
            finish()
        }
    }
}