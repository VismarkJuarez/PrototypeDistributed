package com.example.myapplication.Models

import androidx.room.*

// Defined a non used secondary constructor since this is needed by Room.
// Ideally, in a future release of Room we can remove the secondary constructor
@Entity(indices = [Index(value=["quiz_name"], unique = true), Index(value=["quiz_id"], unique = true)])
data class Quiz(
    @PrimaryKey(autoGenerate = true) val quiz_id: Long,
    val quiz_name : String,
    @Ignore var questions: List<MultipleChoiceQuestion>
){
    constructor(quiz_id: Long, quiz_name: String) : this(quiz_id, quiz_name, arrayListOf<MultipleChoiceQuestion>())
}

