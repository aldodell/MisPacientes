package com.psiqueylogosac.mispacientes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.util.*


@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey() val uid: String,
    var fecha: Date? = Date(),
    var hora: Date? = Date(),
    var pacienteUid: String = "",
    var informe: String? = "",
    var notas: String? = "",
    var honorarios: String? = ""
)


class CitaModelo {

    class Paciente2 {
        var uid: String? = null
        var apellidos: String? = null
        var nombres: String? = null
        fun toHashMap(): Map<String, Any?> = hashMapOf(
            "uid" to uid,
            "apellidos" to apellidos,
            "nombres" to nombres
        )
        fun fromHashMap(map: MutableMap<String, Any>?) {
            uid = map?.get("uid") as String?
            apellidos = map?.get("apellidos") as String?
            nombres = map?.get("nombres") as String?
        }
    }

    var uid: String? = UUID.randomUUID().toString()
    var fechaHora: Date? = Date()
    var pacienteUid: String = ""
    var informe: String? = ""
    var notas: String? = ""
    var honorarios: String? = ""
    var paciente: Paciente2? = Paciente2()

    fun toHashMap(): Map<String, Any?> = hashMapOf(
        "uid" to uid,
        "fechaHora" to fechaHora,
        "pacienteUid" to pacienteUid,
        "informe" to informe,
        "notas" to notas,
        "honorarios" to honorarios,
        "paciente" to paciente?.toHashMap()
    )

    fun fromHashMap(map: MutableMap<String, Any>?) {
        uid = map?.get("uid") as String
        fechaHora = (map["fechaHora"] as Timestamp).toDate()
        pacienteUid = map["pacienteUid"] as String
        informe = map["informe"] as String
        notas = map["notas"] as String
        honorarios = map["honorarios"] as String
        paciente?.fromHashMap(map["paciente"] as MutableMap<String, Any>?)
    }


}


/*
@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey() val uid: String,
    var fecha: Date? = Date(),
    var hora: Date? = Date(),
    var pacienteUid: String = "",
    var informe: String? = "",
    var notas: String? = "",
    var honorarios: String? = ""
)

fun citaHashMap(
    fechaHora: Date? = null,
    pacienteUid: String? = null,
    notas: String? = null,
    honorarios: String? = null
): Map<String, Any?> {
    return hashMapOf(
        "fechaHora" to fechaHora,
        "pacienteUid" to pacienteUid,
        "notas" to notas,
        "honorarios" to honorarios
    )
}

 */