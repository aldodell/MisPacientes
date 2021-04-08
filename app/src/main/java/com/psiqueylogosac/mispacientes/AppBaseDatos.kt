package com.psiqueylogosac.mispacientes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Paciente::class, Cita::class),version = 1)
@TypeConverters(Convertidores::class)
abstract class AppBaseDatos : RoomDatabase() {
    abstract fun pacienteDao() : PacienteDao
    abstract fun citaDao() : CitaDao
}