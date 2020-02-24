package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    @PrimaryKey(autoGenerate = true) val obj_id: Long,
    val answer: String,
    val choices: List<String>,
    val kind: String = "question",
    val object_id: String,
    val prompt: String
)