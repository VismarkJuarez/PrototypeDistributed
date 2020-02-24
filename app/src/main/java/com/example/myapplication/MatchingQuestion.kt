package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MatchingQuestion(
    @PrimaryKey(autoGenerate = true) val obj_id: Long,
    val answer: HashMap<String, String>,
    val kind: String = "question",
    val left_choices: List<String>,
    val object_id: String,
    val prompt: String,
    val right_choices: List<String>,
    val type: String = "matching"
) : QuestionData(id = obj_id)