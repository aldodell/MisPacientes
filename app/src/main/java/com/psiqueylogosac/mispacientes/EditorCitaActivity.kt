package com.psiqueylogosac.mispacientes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import java.util.*


class EditorCitaActivity : AppCompatActivity() {

    lateinit var modo: MODOS
    lateinit var fechaEt: EditText
    lateinit var horaEt: EditText
    lateinit var fechaIb: ImageButton
    lateinit var horaIb: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_cita)

        modo = MODOS.valueOf(intent.getStringExtra("modo")!!)
        fechaEt = findViewById(R.id.editorCitasFechaEt)
        horaEt = findViewById(R.id.editorCitasHoraEt)
        fechaIb = findViewById(R.id.editorCitasFechaIb)
        horaIb = findViewById(R.id.editorCitasHoraIb)

        val calendar = Calendar.getInstance()
        val datePickerDialogListener =
            DatePickerDialog.OnDateSetListener { p0, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val s = "$year/$monthOfYear/$dayOfMonth"
                fechaEt.setText(s)

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
                val s = "$hour:$minute"
                horaEt.setText(s)
            }

        horaIb.setOnClickListener { hb ->
            TimePickerDialog(this.applicationContext, timePickerDialogListener, 8, 0, true)
                .show()
        }

    }
}