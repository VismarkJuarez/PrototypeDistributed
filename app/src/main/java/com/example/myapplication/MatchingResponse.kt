package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = FillInTheBlankQuestion::class, parentColumns = arrayOf("id"), childColumns = arrayOf("parent_question_id"))])
data class MatchingResponse(
    @PrimaryKey(autoGenerate = true) val obj_id: Long,
    val answer: HashMap<String, String>,
    val parent_question_id: Long,
    val kind: String = "response",
    val nickname: String,
    val type: String = "matching",
    val user_id: String
) : ResponseData()