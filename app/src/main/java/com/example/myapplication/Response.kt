package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.lang.RuntimeException

@Entity(foreignKeys = [ForeignKey(entity = Question::class, parentColumns = arrayOf("id"), childColumns = arrayOf("parent_question_id"))])
data class Response(
    @PrimaryKey(autoGenerate = true) val obj_d: Long,
    val parent_question_id: Long,
    val answer: String,
    val kind: String = "response",
    val nickname: String,
    val user_id: String
)