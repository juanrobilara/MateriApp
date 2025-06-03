package com.jurobil.materiapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CarreraEntity::class, AsignaturaEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carreraDao(): CarreraDao
    abstract fun asignaturaDao(): AsignaturaDao
}