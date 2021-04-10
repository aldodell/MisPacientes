package com.psiqueylogosac.mispacientes

import android.content.Intent
import android.util.ArrayMap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers

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

