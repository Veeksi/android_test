package com.example.testapplication.data.local

import androidx.room.*
import com.example.testapplication.domain.model.Character

@Dao
@TypeConverters(Converters::class)
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Query("SELECT * FROM characters")
    fun getAllFavorites(): List<Character>

    @Delete
    fun deleteFromFavorite(character: Character)

    @Query("DELETE FROM characters")
    fun deleteAllFavorites()
}