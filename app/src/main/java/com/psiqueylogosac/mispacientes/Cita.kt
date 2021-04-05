package com.psiqueylogosac.mispacientes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val fecha: String?,
    val hora: String?,
    val pacienteUid: String = "",
    val informe: String?,
    val notas: String?,
    val honorarios: String?
)