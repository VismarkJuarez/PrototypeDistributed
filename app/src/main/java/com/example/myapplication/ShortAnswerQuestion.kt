package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShortAnswerQuestion(
        @PrimaryKey(autoGenerate = true) val obj_id: Long,
        val answer: String,
        val kind: String = "question",
        val type: String = "short_answer",
        val object_id: String,
        val prompt: String
) : QuestionData(obj_id)
