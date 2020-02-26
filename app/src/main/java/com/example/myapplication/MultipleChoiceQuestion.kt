package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MultipleChoiceQuestion(
    @PrimaryKey(autoGenerate = true) val question_id: Long,
    val answer: String,
    val choices: List<String>,
    val prompt: String
)