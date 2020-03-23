package com.example.myapplication.Models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Parcelize
@Entity(foreignKeys = [ForeignKey(entity = MultipleChoiceQuestion::class, parentColumns = arrayOf("question_id"), childColumns = arrayOf("parent_question_id"), onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = User::class, parentColumns = arrayOf("user_id"), childColumns = arrayOf("user_id"), onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Quiz::class, parentColumns = arrayOf("quiz_id"), childColumns = arrayOf("quiz_id"), onDelete = ForeignKey.CASCADE)],
    indices = [Index(value=["parent_question_id"]), Index(value=["user_id"]), Index(value=["quiz_id"])])
data class MultipleChoiceResponse(
    @PrimaryKey val response_id: String,
    val parent_question_id: String,
    val answer: String,
    val user_id: String,
    val quiz_id: String
) : Parcelable