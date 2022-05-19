package com.example.testapplication.domain.model

import android.os.Parcelable
import androidx.room.*
import com.example.testapplication.data.local.Converters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "characters")
data class Character(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val image: String,
    val gender: String,
    var liked: Boolean = false,
    @Embedded
    val location: Location? = null,
    val created: String? = null,
    @Embedded
    val origin: Origin? = null,
    val species: String? = null,
    val status: String? = null,
    val type: String? = null,
    val episodes: List<String> = emptyList()
) : Parcelable
