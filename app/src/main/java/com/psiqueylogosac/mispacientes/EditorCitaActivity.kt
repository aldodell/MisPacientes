package com.psiqueylogosac.mispacientes

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.Temporal
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
    var paciente = PacienteModelo()
    var citaUid: String? = null
    lateinit var db: FirebaseFirestore

    var cita = CitaModelo()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_cita)

        //Singleton de firestore
        db = FirebaseFirestore.getInstance()


        //Obtenemo el modo y otros datos desde el activity que llamó
        modo = MODOS.valueOf(intent.getStringExtra("modo")!!)
        pacienteUid = intent.getStringExtra("pacienteUid")
        citaUid = intent.getStringExtra("citaUid")
        cita.pacienteUid = pacienteUid!!

        //Si no se pasó un citaId se crea uno
        if (citaUid == null) citaUid = cita.uid

        //Obtenemos las referencias a los VIEWs
        fechaEt = findViewById(R.id.editorCitasFechaEt)
        horaEt = findViewById(R.id.editorCitasHoraEt)
        fechaIb = findViewById(R.id.editorCitasFechaIb)
        horaIb = findViewById(R.id.editorCitasHoraIb)
        otrasCitas = findViewById(R.id.editorCitasOtrasCitasTv)
        descartarBt = findViewById(R.id.editorCitasDescartarBt)
        salvarBt = findViewById(R.id.editorCitasSalvarBt)
        notasEt = findViewById(R.id.editorCitasNotasEt)
        honorariosEt = findViewById(R.id.editorCitasHonorariosEt)

        //Cargamos el paciente actual
        db.document("usuarios/$usuarioId/pacientes/$pacienteUid")
            .get()
            .addOnSuccessListener {
                paciente.fromHashMap(it.data)
            }


        //Cargamos los datos de la cita si es modo editar
        if (modo == MODOS.EDITAR) {
            db.document("usuarios/$usuarioId/citas/$citaUid")
                .get().addOnSuccessListener {
                    cita.fromHashMap(it.data)

                    //Cargamos datos iniciales:
                    fechaEt.setText(formateadorFecha.format(cita.fechaHora!!))
                    horaEt.setText(formateadorHora.format(cita.fechaHora!!))
                    notasEt.setText(cita.notas)
                    honorariosEt.setText(cita.honorarios)
                }
                .addOnFailureListener {
                    Log.d("aldox", it.message!!)
                    Toast.makeText(this.baseContext, it.message, Toast.LENGTH_LONG).show()
                }

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
            salvarCita()
            finish()
        }


    }

    fun salvarCita() {


        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val fecha =
            formateadorFecha.format(
                formateadorFecha.parse(
                    fechaEt.text.toString().replace("/", "-")
                )
            )


        @Suppress(
            "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
            "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
        )
        val hora = formateadorHora.format(
            formateadorHora.parse(
                horaEt.text.toString()
            )
        )

        val miFechaHora = formateadorFechaHora.parse("$fecha $hora")

        cita.apply {
            fechaHora = miFechaHora
            notas = notasEt.text.toString()
            honorarios = honorariosEt.text.toString()
        }

        cita.pacienteUid = pacienteUid as String

        cita.paciente?.apply {
            apellidos = paciente.apellidos
            nombres = paciente.nombres
            uid = paciente.uid
        }



        /* Guardar en la firestore */
        db.collection("usuarios")
            .document(usuarioId)
            .collection("citas")
            .document(cita.uid!!)
            .set(cita.toHashMap())


    }
}