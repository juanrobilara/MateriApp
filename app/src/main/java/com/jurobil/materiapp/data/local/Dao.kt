package com.jurobil.materiapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CarreraDao {
    @Query("SELECT * FROM carreras")
    suspend fun getAll(): List<CarreraEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(carreras: List<CarreraEntity>)

    @Query("DELETE FROM carreras")
    suspend fun clearAll()
}

@Dao
interface AsignaturaDao {
    @Query("SELECT * FROM asignaturas WHERE carreraId = :carreraId ORDER BY numero ASC")
    suspend fun getByCarreraId(carreraId: String): List<AsignaturaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asignaturas: List<AsignaturaEntity>)

    @Query("DELETE FROM asignaturas WHERE carreraId = :carreraId")
    suspend fun clearByCarreraId(carreraId: String)
}