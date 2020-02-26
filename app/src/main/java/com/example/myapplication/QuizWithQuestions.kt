package com.example.myapplication

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class QuizWithQuestions(
    @Embedded val quiz: Quiz,
    @Relation(
        parentColumn = "quiz_id",
        entityColumn = "question_id",
        associateBy = Junction(QuizQuestionCrossRef::class)
    )
    val questions: List<MultipleChoiceQuestion>
)
