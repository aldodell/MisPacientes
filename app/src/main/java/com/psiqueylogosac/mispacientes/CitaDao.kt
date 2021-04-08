package com.psiqueylogosac.mispacientes

import androidx.room.*
import java.util.*

@Dao
interface CitaDao {

    @Query("select * from citas order by fecha, hora")
    fun todos(): List<Cita>

    @Query("select * from citas where fecha > :aPartirDe order by fecha, hora ")
    fun todos(aPartirDe: Date): List<Cita>


    @Query("select * from citas where pacienteUid = :pacienteUid")
    fun porPaciente(pacienteUid: String): List<Cita>

    @Transaction
    @Query("select * from citas where fecha = :fecha")
    fun porFecha(fecha: Date) : List<CitasConPaciente>

    @Transaction
    @Query("select * from citas where fecha  >= :fecha order by fecha, hora")
    fun aPartirDe(fecha:Date) : List<CitasConPaciente>


    @Insert
    fun insertar(vararg citas: Cita)

    @Delete
    fun eliminar(vararg citas: Cita)

    @Update
    fun actualizar(vararg citas: Cita)


}