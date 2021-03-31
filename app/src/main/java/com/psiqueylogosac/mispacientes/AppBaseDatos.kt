package com.psiqueylogosac.mispacientes

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Paciente::class),version = 1)
abstract class AppBaseDatos : RoomDatabase() {
    abstract fun pacienteDao() : PacienteDao
}