package com.psiqueylogosac.mispacientes

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AjustesActivity : AppCompatActivity() {

    lateinit var correoElectronicoEt: EditText
    lateinit var contrasenaEt: EditText
    lateinit var contrasenaConfirmacionEt: EditText
    lateinit var crearUsuario: ImageButton
    lateinit var identificarUsuario: ImageButton
    lateinit var barraProgreso : ProgressBar
    lateinit var crearUsuarioEtiqueta : TextView
    lateinit var identificarUsuarioEtiqueta : TextView

    val autorizador = FirebaseAuth.getInstance()
    lateinit var prefs: SharedPreferences

    var correoElectronico = ""
    var contrasena = ""
    var contrasenaConfirmacion = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)

        // Identificamos los componentes
        correoElectronicoEt = findViewById(R.id.ajustesCorreoElectronico)
        contrasenaEt = findViewById(R.id.ajustesContrasena)
        contrasenaConfirmacionEt = findViewById(R.id.ajustesContrasenaConfirmacion)
        crearUsuario = findViewById(R.id.ajustesCrearUsuario)
        identificarUsuario = findViewById(R.id.ajustesIdentificarUsuario)
        barraProgreso = findViewById(R.id.ajustesProgreso)
        crearUsuarioEtiqueta = findViewById(R.id.ajustesCrearUsuarioEtiqueta)
        identificarUsuarioEtiqueta = findViewById(R.id.ajustesIdentificarUsaurioEtiqueta)




        //Inhabilitamos botones
        configurarInterfaz(false,false, false)


        //Obtenemos acceso a las preferencias
        prefs = this.getSharedPreferences("ajustes", MODE_PRIVATE)

        //Si el correo no es null entonces procesamos la autorizacion
        if (prefs.getString("correoElectronico", null) != null) {
            correoElectronicoEt.setText(prefs.getString("correoElectronico", ""))
            contrasenaEt.setText(prefs.getString("contrasena", ""))
            contrasena = contrasenaEt.text.toString().trim()
            correoElectronico = correoElectronicoEt.text.toString().trim()
            configurarInterfaz(false,true)
        } else {
            configurarInterfaz(true,false)
        }


        correoElectronicoEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(correoElectronico != correoElectronicoEt.text.toString().trim())
                {
                    configurarInterfaz(true,true,false)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                correoElectronico = correoElectronicoEt.text.toString().trim()
            }

        })



        contrasenaEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                contrasena = contrasenaEt.text.toString().trim()
            }

        })


        contrasenaConfirmacionEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                contrasenaConfirmacion = contrasenaConfirmacionEt.text.toString().trim()
            }

        })






        contrasenaEt.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                contrasena = contrasenaEt.text.toString().trim()
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
                if (contrasenaConfirmacion.toString().length > 1) {
                    if (contrasena != contrasenaConfirmacion) {
                        AlertDialog.Builder(view.context)
                            .setMessage(R.string.contrasenas_no_coinciden)
                            .setNeutralButton(R.string.ok) { d, _ -> d.dismiss() }
                            .show()

                    }
                }
            }
        }

        //Creamos un usuario nuevo. Hay que verificar si ya existe en la base de datos
        crearUsuario.setOnClickListener {
            configurarInterfaz(false,false,true)

            if(contrasena != contrasenaConfirmacion) {
                AlertDialog.Builder(it.context)
                    .setMessage(R.string.contrasenas_no_coinciden)
                    .setNeutralButton(R.string.ok) { _,_ -> configurarInterfaz(true,true,false) }
                    .show()

            } else {

                autorizador.createUserWithEmailAndPassword(
                    correoElectronico,
                    contrasena
                ).addOnCompleteListener(this) { task ->
                    configurarInterfaz(false, true, false)
                    if (task.isSuccessful) {
                        guardarUsuarioLocalmente()
                        AlertDialog.Builder(it.context)
                            .setMessage(R.string.usuario_creado)
                            .setNeutralButton(R.string.ok) { _, _ ->
                                finish()
                            }
                            .show()
                        configurarInterfaz(false, true)
                    } else {
                        AlertDialog.Builder(it.context)
                            .setMessage(getString(R.string.error_creando_usuario) + " " + task.exception!!.message!!)
                            .setNeutralButton(R.string.ok) { d, _ -> d.dismiss() }
                            .show()
                    }
                }
            }
        }

        //Identificamos el usuario (log in)
        identificarUsuario.setOnClickListener {
            configurarInterfaz(false,false,true)
            autorizador.signInWithEmailAndPassword(correoElectronico, contrasena)
                .addOnCompleteListener(this)
                { task ->
                    configurarInterfaz(false,true,false)
                    if (task.isSuccessful) {
                        guardarUsuarioLocalmente()
                        AlertDialog.Builder(it.context)
                            .setMessage(R.string.acceso_positivo)
                            .setNeutralButton(R.string.ok) { _,_ -> finish()  }
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

    fun configurarInterfaz(crear: Boolean?, identificar : Boolean?, procesando: Boolean?=null){

        crearUsuario.isEnabled = false
        identificarUsuario.isEnabled = false
        crearUsuarioEtiqueta.isEnabled = false
        identificarUsuarioEtiqueta.isEnabled = false



        crear?.let {
            correoElectronicoEt.isEnabled = it
            contrasenaEt.isEnabled = it
            contrasenaConfirmacionEt.isEnabled = it
            crearUsuarioEtiqueta.isEnabled = it
            crearUsuario.isEnabled = it
        }

        identificar?.let {
            correoElectronicoEt.isEnabled = it
            contrasenaEt.isEnabled = it
            contrasenaConfirmacionEt.isEnabled = it
            identificarUsuarioEtiqueta.isEnabled = it
            identificarUsuario.isEnabled = it
        }


        procesando?.let {
            if(it) {
                barraProgreso.visibility = View.VISIBLE
            } else {
                barraProgreso.visibility = View.INVISIBLE
            }
        }


    }





}