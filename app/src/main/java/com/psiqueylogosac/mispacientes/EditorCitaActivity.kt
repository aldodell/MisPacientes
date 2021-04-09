package com.psiqueylogosac.mispacientes

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import java.text.SimpleDateFormat
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

    var pacienteUid: String? = null
    var citaUid: Int? = 0
    var cita = Cita(0, Date(), Date(), "", "", "", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_cita)

        modo = MODOS.valueOf(intent.getStringExtra("modo")!!)
        pacienteUid = intent.getStringExtra("pacienteUid")
        citaUid = intent.getIntExtra("citaUid", 0)
        cita.pacienteUid = pacienteUid!!

        if (citaUid == null) citaUid = 0

        //  Log.i("aldox", pacienteUid!!)
        // Log.i("aldox", citaUid!!.toString())


        fechaEt = findViewById(R.id.editorCitasFechaEt)
        horaEt = findViewById(R.id.editorCitasHoraEt)
        fechaIb = findViewById(R.id.editorCitasFechaIb)
        horaIb = findViewById(R.id.editorCitasHoraIb)
        otrasCitas = findViewById(R.id.editorCitasOtrasCitasTv)
        descartarBt = findViewById(R.id.editorCitasDescartarBt)
        salvarBt = findViewById(R.id.editorCitasSalvarBt)
        notasEt = findViewById(R.id.editorCitasNotasEt)
        honorariosEt = findViewById(R.id.editorCitasHonorariosEt)


        //Si el modo es EDITAR entonces cargamos los datos iniciales
        if (modo == MODOS.EDITAR) {
            Thread {
                //Cargamos los datos
                cita = baseDatos.citaDao().porUid(citaUid!!.toString())
                runOnUiThread {
                    fechaEt.setText(formateadorFecha.format(cita.fecha!!))
                    horaEt.setText(formateadorHora.format(cita.hora!!))
                    notasEt.setText(cita.notas)
                    honorariosEt.setText(cita.honorarios)
                }
            }.start()
        }

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
                                "$r " + formateadorHora.format(
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
                timePicker.setIs24HourView(false)
                val d = SimpleDateFormat("hh:mm").parse("$hour:$minute")
                val s = formateadorHora.format(d!!)
                horaEt.setText(s)
            }

        horaIb.setOnClickListener { hb ->
            TimePickerDialog(hb.context, timePickerDialogListener, 8, 0, false)
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


        //Guardamos la cita
        salvarBt.setOnClickListener {
            Thread {
                //Reformulamos la cita
                cita.apply {
                    fecha = formateadorFecha.parse(
                        fechaEt.text.toString().replace("/", "-")
                    )
                    hora = formateadorHora.parse(
                        horaEt.text.toString()
                    )
                    notas = notasEt.text.toString()
                    honorarios = honorariosEt.text.toString()
                }

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


    }
}