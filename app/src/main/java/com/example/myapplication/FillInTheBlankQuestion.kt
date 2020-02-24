package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FillInTheBlankQuestion(
        @PrimaryKey(autoGenerate = true) val obj_id: Long,
        val kind: String = "question",
        val type: String = "fill_in_the_blank",
        val before_prompt: String,
        val after_prompt: String,
        val answer: String
) : QuestionData(id = obj_id)


