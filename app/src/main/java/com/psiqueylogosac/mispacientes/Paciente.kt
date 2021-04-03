package com.psiqueylogosac.mispacientes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="pacientes")
data class Paciente (
        @PrimaryKey val uid : String,
        val apellidos : String?,
        val nombres : String?,
        val sexo:String?,
        val cedula : String?,
        val fechaNacimiento : String?,
        val anamnesis : String?,
        val notas : String?,
        val celular : String?,
        val correoElectronico: String?

        )