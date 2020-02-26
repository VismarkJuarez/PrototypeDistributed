package com.example.myapplication

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value=["quiz_name"], unique = true)])
data class Quiz(
    @PrimaryKey(autoGenerate = true) val quiz_id: Long,
    val quiz_name : String
)