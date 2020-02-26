package com.example.myapplication

import androidx.room.Entity

@Entity(primaryKeys = ["question_id", "quiz_id"])
data class QuizQuestionCrossRef(
    val question_id: Long,
    val quiz_id: Long
)
