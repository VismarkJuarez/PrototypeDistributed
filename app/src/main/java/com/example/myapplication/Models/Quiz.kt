package com.example.myapplication.Models

import androidx.room.*
import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

// Defined a non used secondary constructor since this is needed by Room.
// Ideally, in a future release of Room we can remove the secondary constructor
@Serializable
@Entity(indices = [Index(value=["quiz_name"], unique = true), Index(value=["quiz_id"], unique = true)])
data class Quiz(
    @SerialId(1) @PrimaryKey(autoGenerate = true) val quiz_id: Long,
    @SerialId(2) val quiz_name : String,
    @SerialId(3) @Ignore var questions: List<MultipleChoiceQuestion> = emptyList()
){
    constructor(quiz_id: Long, quiz_name: String) : this(quiz_id, quiz_name, arrayListOf<MultipleChoiceQuestion>())
}

