package com.jurobil.materiapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jurobil.materiapp.data.database.dao.AsignaturaDao
import com.jurobil.materiapp.data.database.entities.AsignaturaEntity
import com.jurobil.materiapp.data.database.dao.CarreraDao
import com.jurobil.materiapp.data.database.entities.CarreraEntity

@Database(entities = [CarreraEntity::class, AsignaturaEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carreraDao(): CarreraDao
    abstract fun asignaturaDao(): AsignaturaDao
}