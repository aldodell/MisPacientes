package com.psiqueylogosac.mispacientes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase
import java.util.*

@Entity(tableName = "pacientes")
data class Paciente(
    @PrimaryKey val uid: String,
    val apellidos: String?,
    val nombres: String?,
    val sexo: String?,
    val cedula: String?,
    val fechaNacimiento: Date?,
    val anamnesis: String?,
    val notas: String?,
    val celular: String?,
    val correoElectronico: String?

)


fun pacienteHashMap(
    apellidos: String? = null,
    nombres: String? = null,
    sexo: String? = null,
    cedula: String? = null,
    fechaNacimiento: Date? = null,
    anamnesis: String? = null,
    notas: String? = null,
    celular: String? = null,
    correoElectronico: String? = null


): Map<String, Any?> {

    return hashMapOf(
        "apellidos" to apellidos,
        "nombres" to nombres,
        "sexo" to sexo,
        "cedula" to cedula,
        "fechaNacimiento" to fechaNacimiento,
        "anamnesis" to anamnesis,
        "notas" to notas,
        "celular" to celular,
        "correoElectronico" to correoElectronico
    )
}


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