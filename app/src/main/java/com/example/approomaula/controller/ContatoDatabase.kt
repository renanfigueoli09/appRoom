package com.example.approomaula.controller

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Contato::class],
    version = 1
)

abstract class ContatoDatabase: RoomDatabase() {
    abstract val dao: ContatoDao
}