package com.example.myapplication

import androidx.room.*

@Entity(indices = [Index(value=["quiz_name"], unique = true), Index(value=["quiz_id"], unique = true)])
data class Quiz(
    @PrimaryKey(autoGenerate = true) val quiz_id: Long,
    val quiz_name : String,
    @Ignore var questions: List<MultipleChoiceQuestion>
){
    constructor(quiz_id: Long, quiz_name: String) : this(quiz_id, quiz_name, arrayListOf<MultipleChoiceQuestion>())
}

