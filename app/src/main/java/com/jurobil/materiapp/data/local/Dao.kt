package com.jurobil.materiapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarreraDao {
    @Query("SELECT * FROM carreras")
    fun getAllFlow(): Flow<List<CarreraEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(carreras: List<CarreraEntity>)

    @Query("DELETE FROM carreras")
    suspend fun clearAll()

    @Query("DELETE FROM carreras WHERE id = :id")
    suspend fun deleteById(id: String)
}

@Dao
interface AsignaturaDao {

    @Query("SELECT * FROM asignaturas WHERE carreraId = :carreraId ORDER BY numero ASC")
    suspend fun getByCarreraId(carreraId: String): List<AsignaturaEntity>

    @Query("SELECT * FROM asignaturas ORDER BY numero ASC")
    fun getAllFlow(): Flow<List<AsignaturaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asignaturas: List<AsignaturaEntity>)

    @Query("DELETE FROM asignaturas WHERE carreraId = :carreraId")
    suspend fun clearByCarreraId(carreraId: String)
}