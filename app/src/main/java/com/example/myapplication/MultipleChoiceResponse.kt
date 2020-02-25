package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = MultipleChoiceQuestion::class, parentColumns = arrayOf("id"), childColumns = arrayOf("parent_question_id"))])
data class MultipleChoiceResponse(
    @PrimaryKey(autoGenerate = true) val obj_d: Long,
    val parent_question_id: Long,
    val answer: String,
    val user_id: Long,
    val session_id: Long
)