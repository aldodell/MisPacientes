package com.psiqueylogosac.mispacientes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    var fecha: Date? = Date(),
    var hora: Date? = Date(),
    var pacienteUid: String = "",
    var informe: String? = "",
    var notas: String? = "",
    var honorarios: String? = ""
)