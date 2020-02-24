package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = FillInTheBlankQuestion::class, parentColumns = arrayOf("id"), childColumns = arrayOf("parent_question_id"))])
data class FillInTheBlankResponse(
        @PrimaryKey(autoGenerate = true) val obj_id: Long,
        val parent_question_id: Int,
        val answer: String,
        val kind: String = "response",
        val nickname: String,
        val type: String = "fill_in_the_blank",
        val user_id: String
) : ResponseData()