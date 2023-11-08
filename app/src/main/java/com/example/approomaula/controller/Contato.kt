package com.example.approomaula.controller

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contatos")
data class Contato(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val sobrenome: String,
    val telefone: String
)
