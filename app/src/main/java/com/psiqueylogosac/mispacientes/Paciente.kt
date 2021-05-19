package com.psiqueylogosac.mispacientes

import com.google.firebase.Timestamp
import java.util.*


open class PacienteModelo() {

    var uid: String? = UUID.randomUUID().toString()
    var apellidos: String? = null
    var nombres: String? = null
    var sexo: String? = null
    var cedula: String? = null
    var fechaNacimiento: Date? = Date()
    var anamnesis: String? = null
    var notas: String? = null
    var celular: String? = null
    var correoElectronico: String? = null
    var activo: Boolean = true

    fun toHashMap(): Map<String, Any?> =
        hashMapOf(
            "apellidos" to apellidos,
            "nombres" to nombres,
            "sexo" to sexo,
            "cedula" to cedula,
            "fechaNacimiento" to Timestamp(fechaNacimiento as Date),
            "anamnesis" to anamnesis,
            "notas" to notas,
            "celular" to celular,
            "correoElectronico" to correoElectronico,
            "activo" to activo,
            "uid" to uid
        )

    fun fromHashMap(map: MutableMap<String, Any>?) {
        uid = map?.get("uid") as String
        apellidos = map.get("apellidos") as String
        nombres = map.get("nombres") as String
        sexo = map.get("sexo") as String
        cedula = map.get("cedula") as String
        fechaNacimiento = (map.get("fechaNacimiento") as Timestamp).toDate()
        anamnesis = map.get("anamnesis") as String
        notas = map.get("notas") as String
        correoElectronico = map?.get("correoElectronico") as String
        celular = map.get("celular") as String
        activo = map.get("activo") as Boolean
    }
}