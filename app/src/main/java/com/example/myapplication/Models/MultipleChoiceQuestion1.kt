package com.example.myapplication.Models

import androidx.room.PrimaryKey

data class MultipleChoiceQuestion1 (
    val quiz_id: String,
    val question_id: String,
    val prompt: String,
    val choices: List<String>,
    val answer: String
) : java.io.Serializable {

    override fun toString(): String{
        val multipleChoiceQuestionAsString = "{quizId: ${quiz_id}, choices: ${choices} , questionId: ${question_id}, prompt: ${prompt}, answer: ${answer}}"
        return multipleChoiceQuestionAsString
    }
}