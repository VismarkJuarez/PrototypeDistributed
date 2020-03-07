package com.example.myapplication.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class MultipleChoiceQuestion(
    @SerialId(1) @PrimaryKey(autoGenerate = true) val question_id: Long,
    @SerialId(2) val answer: String,
    @SerialId(3) val choices: List<String>,
    @SerialId(4) val prompt: String,
    @SerialId(5) val quiz_id: Long
)