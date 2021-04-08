package com.psiqueylogosac.mispacientes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val fecha: Date?,
    val hora: Date?,
    val pacienteUid: String = "",
    val informe: String?,
    val notas: String?,
    val honorarios: String?
)