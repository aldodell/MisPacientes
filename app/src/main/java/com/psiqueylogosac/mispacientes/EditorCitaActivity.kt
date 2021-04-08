package com.psiqueylogosac.mispacientes

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import java.util.*


class EditorCitaActivity : AppCompatActivity() {

    lateinit var modo: MODOS
    lateinit var fechaEt: EditText
    lateinit var horaEt: EditText
    lateinit var fechaIb: ImageButton
    lateinit var horaIb: ImageButton
    lateinit var otrasCitas: EditText
    lateinit var descartarBt: Button
    lateinit var salvarBt: Button
    lateinit var notasEt: EditText
    lateinit var honorariosEt: EditText

    var pacienteUid = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_cita)

        modo = MODOS.valueOf(intent.getStringExtra("modo")!!)
        pacienteUid = intent.getStringExtra("pacienteUid")!!


        fechaEt = findViewById(R.id.editorCitasFechaEt)
        horaEt = findViewById(R.id.editorCitasHoraEt)
        fechaIb = findViewById(R.id.editorCitasFechaIb)
        horaIb = findViewById(R.id.editorCitasHoraIb)
        otrasCitas = findViewById(R.id.editorCitasOtrasCitasTv)
        descartarBt = findViewById(R.id.editorCitasDescartarBt)
        salvarBt = findViewById(R.id.editorCitasSalvarBt)
        notasEt = findViewById(R.id.editorCitasNotasEt)
        honorariosEt = findViewById(R.id.editorCitasHonorariosEt)


        //Configuración de los Date y Time pickers
        val calendar = Calendar.getInstance()
        val datePickerDialogListener =
            DatePickerDialog.OnDateSetListener { p0, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val m = monthOfYear + 1
                val s = "$dayOfMonth-$m-$year"
                val d = formateadorFecha.parse(s)
                fechaEt.setText(
                    formateadorFecha.format(d!!)
                )


                Thread {
                    var r = getString(R.string.otras_citas_asignadas) + "\n"
                    val mCitas = baseDatos
                        .citaDao()
                        .porFecha(d)
                    if (mCitas.isNotEmpty()) {
                        mCitas.forEach { x ->
                            r = r + x.paciente.apellidos + " " + x.paciente.nombres
                            r =
                                r + " " + formateadorHora.format(
                                    x.cita.hora!!
                                ) + "\n"
                        }
                    } else {
                        r = getString(R.string.no_hay_citas_asignadas)
                    }
                    runOnUiThread {
                        otrasCitas.setText(r)
                    }
                }.start()

            }

        fechaIb.setOnClickListener {
            DatePickerDialog(
                it.context,
                datePickerDialogListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

            ).show()

        }


        val timePickerDialogListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                horaEt.setText(
                    formateadorHora.format(formateadorHora.parse("$hour:$minute")!!)
                )
            }

        horaIb.setOnClickListener { hb ->
            TimePickerDialog(hb.context, timePickerDialogListener, 8, 0, true)
                .show()
        }

        descartarBt.setOnClickListener {
            AlertDialog
                .Builder(it.context)
                .setTitle(R.string.desea_descartar)
                .setPositiveButton(R.string.si) { dialog, pos ->
                    finish()
                }
                .setNegativeButton(R.string.no) { d, p -> }
                .show()
        }

        salvarBt.setOnClickListener {
            Thread {
                val cita = Cita(
                    0,

                    formateadorFecha.parse(
                        fechaEt.text.toString()
                    ),
                    formateadorHora.parse(
                        horaEt.text.toString()
                    ),
                    pacienteUid,
                    "",
                    notasEt.text.toString(),
                    honorariosEt.text.toString()
                )

                when (modo) {
                    MODOS.CREAR -> baseDatos.citaDao().insertar(cita)
                    MODOS.EDITAR -> baseDatos.citaDao().actualizar(cita)
                    else -> {
                    }
                }

                runOnUiThread {
                    finish()
                }


            }.start()
        }

        //Si el modo es EDITAR entonces cargamos los datos iniciales

    }
}