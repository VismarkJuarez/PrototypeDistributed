package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.lang.RuntimeException

@Entity(foreignKeys = [ForeignKey(entity = FillInTheBlankQuestion::class, parentColumns = arrayOf("id"), childColumns = arrayOf("parent_question_id"))])
data class MultipleChoiceResponse(
    @PrimaryKey(autoGenerate = true) val obj_id: Long,
    val parent_question_id: Long,
    val answer: String,
    val kind: String = "response",
    val nickname: String,
    val type: String = "multiple_choice",
    val user_id: String
) : ResponseData()