package com.example.myapplication.Models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Parcelize
@Entity
data class User(
    @PrimaryKey val user_id: String,
    val nickname: String
) : Parcelable