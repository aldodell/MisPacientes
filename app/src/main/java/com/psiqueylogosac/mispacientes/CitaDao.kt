package com.psiqueylogosac.mispacientes

import androidx.room.*

@Dao
interface CitaDao {

    @Query("select * from citas order by fecha, hora")
    fun todos(): List<Cita>

    @Query("select * from citas where pacienteUid = :pacienteUid")
    fun porPaciente(pacienteUid: String): List<Cita>

    @Insert
    fun insertar(vararg citas: Cita)

    @Delete
    fun eliminar(vararg citas: Cita)

    @Update
    fun actualizar(vararg citas: Cita)


}