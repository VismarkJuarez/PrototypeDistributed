package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(foreignKeys = [ForeignKey(entity = MultipleChoiceQuestion::class, parentColumns = arrayOf("question_id"), childColumns = arrayOf("parent_question_id"), onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = User::class, parentColumns = arrayOf("user_id"), childColumns = arrayOf("user_id"), onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Quiz::class, parentColumns = arrayOf("quiz_id"), childColumns = arrayOf("quiz_id"), onDelete = ForeignKey.CASCADE)],
    indices = [Index(value=["parent_question_id"]), Index(value=["user_id"]), Index(value=["quiz_id"])])
data class MultipleChoiceResponse(
    @PrimaryKey(autoGenerate = true) val response_id: Long,
    val parent_question_id: Long,
    val answer: String,
    val user_id: Long,
    val quiz_id: Long
)