package com.psiqueylogosac.mispacientes

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AjustesActivity : AppCompatActivity() {

    lateinit var correoElectronicoEt: EditText
    lateinit var contrasenaEt: EditText
    lateinit var contrasenaConfirmacionEt: EditText
    lateinit var crearUsuario: ImageButton
    lateinit var identificarUsuario: ImageButton

    // lateinit var respaldarDatos: Button
    //  lateinit var recuperarDatos: Button
    val autorizador = FirebaseAuth.getInstance()
    lateinit var prefs: SharedPreferences

    var correoElectronico = ""
    var contrasena = ""
    var contrasenaConfirmacion = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)


        correoElectronicoEt = findViewById(R.id.ajustesCorreoElectronico)
        contrasenaEt = findViewById(R.id.ajustesContrasena)
        contrasenaConfirmacionEt = findViewById(R.id.ajustesContrasenaConfirmacion)
        crearUsuario = findViewById(R.id.ajustesCrearUsuario)
        identificarUsuario = findViewById(R.id.ajustesIdentificarUsuario)

        crearUsuario.isEnabled = false
        identificarUsuario.isEnabled = false

        // respaldarDatos = findViewById(R.id.ajustesRespaldarDatos)
        // recuperarDatos = findViewById(R.id.ajustesRecuperarDatos)

        // respaldarDatos.isEnabled = false
        // recuperarDatos.isEnabled = false


        prefs = this.getSharedPreferences("ajustes", MODE_PRIVATE)

        if (prefs.getString("correoElectronico", null) != null) {
            correoElectronicoEt.setText(prefs.getString("correoElectronico", ""))
            contrasenaEt.setText(prefs.getString("contrasena", ""))
            contrasena = contrasenaEt.text.toString().trim()
            correoElectronico = correoElectronicoEt.text.toString().trim()
            identificarUsuario.isEnabled = true
        }


        correoElectronicoEt.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                correoElectronico = correoElectronicoEt.text.toString().trim()


            }
        }


        contrasenaEt.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                contrasena = contrasenaEt.text.toString().trim()
                identificarUsuario.isEnabled = true
                if (contrasena.length < 6) {
                    AlertDialog.Builder(view.context)
                        .setMessage(R.string.contrasena_invalida)
                        .setNeutralButton(R.string.ok) { d, _ -> d.dismiss() }
                        .show()
                }
            }


        }


        contrasenaConfirmacionEt.setOnFocusChangeListener { view, hasFocus ->

            if (!hasFocus) {
                crearUsuario.isEnabled = true
                contrasenaConfirmacion = contrasenaConfirmacionEt.text.toString().trim()
                if (contrasenaConfirmacion.toString().length > 1) {
                    if (contrasena != contrasenaConfirmacion) {
                        AlertDialog.Builder(view.context)
                            .setMessage(R.string.contrasenas_no_coinciden)
                            .setNeutralButton(R.string.ok) { d, _ -> d.dismiss() }
                            .show()
                    } else {
                        crearUsuario.isEnabled = true
                    }
                }
            }
        }

        //Creamos un usuario nuevo. Hay que verificar si ya existe en la base de datos
        crearUsuario.setOnClickListener {
            autorizador.createUserWithEmailAndPassword(
                correoElectronico,
                contrasena
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    guardarUsuarioLocalmente()

                    AlertDialog.Builder(it.context)
                        .setMessage(R.string.usuario_creado)
                        .setNeutralButton(R.string.ok) { d, _ -> d.dismiss() }
                        .show()
                } else {
                    AlertDialog.Builder(it.context)
                        .setMessage(getString(R.string.error_creando_usuario) + " " + task.exception!!.message!!)
                        .setNeutralButton(R.string.ok) { d, _ -> d.dismiss() }
                        .show()
                }
            }

        }

        //Identificamos el usuario (log in)
        identificarUsuario.setOnClickListener {
            autorizador.signInWithEmailAndPassword(correoElectronico, contrasena)
                .addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        guardarUsuarioLocalmente()

                        AlertDialog.Builder(it.context)
                            .setMessage(R.string.acceso_positivo)
                            .setNeutralButton(R.string.ok) { d, _ -> d.dismiss() }
                            .show()
                    } else {
                        AlertDialog.Builder(it.context)
                            .setMessage(getString(R.string.error_creando_usuario) + " " + task.exception!!.message!!)
                            .setNeutralButton(R.string.ok) { d, _ -> d.dismiss() }
                            .show()
                    }

                }
        }
    }

    fun guardarUsuarioLocalmente() {
        usuario = autorizador.currentUser
        usuarioId = usuario?.uid!!

        prefs.edit().apply {
            putString("correoElectronico", correoElectronico)
            putString("contrasena", contrasena)
            putString("usuarioId", usuarioId)
        }.apply()


    }

}