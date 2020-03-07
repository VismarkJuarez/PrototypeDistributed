package com.example.myapplication.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class User(
    @SerialId(1) @PrimaryKey(autoGenerate = true) val user_id: Long,
    @SerialId(2) val nickname: String
)