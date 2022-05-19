package com.example.testapplication.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    @ColumnInfo(name = "location_name")  val name: String,
) : Parcelable
