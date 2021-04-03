package com.psiqueylogosac.mispacientes

import androidx.room.*

@Dao
interface PacienteDao {
    @Query("select * from pacientes order by apellidos, nombres")
    fun todos() : List<Paciente>

    @Query("select * from pacientes where cedula = :cedula")
    fun porCedula(cedula: String) : Paciente

    @Query("select * from pacientes where uid = :uid")
    fun porUid(uid: String) : Paciente

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertar(vararg pacientes : Paciente)

    @Update
    fun actualizar(vararg pacientes : Paciente)

    @Delete
    fun eliminar(vararg  pacientes: Paciente)
}