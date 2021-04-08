package com.psiqueylogosac.mispacientes

import androidx.room.Embedded
import androidx.room.Relation


data class CitasConPaciente(
    @Embedded val cita: Cita,
    @Relation(parentColumn = "pacienteUid", entityColumn = "uid")
    val paciente: Paciente
)


