package com.psiqueylogosac.mispacientes

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Paciente::class, Cita::class),version = 1)
abstract class AppBaseDatos : RoomDatabase() {
    abstract fun pacienteDao() : PacienteDao
    abstract fun citaDato() : CitaDao
}