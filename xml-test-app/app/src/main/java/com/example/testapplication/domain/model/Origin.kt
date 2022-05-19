package com.example.testapplication.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Origin(
     @ColumnInfo(name = "origin_name") val name: String
) : Parcelable
