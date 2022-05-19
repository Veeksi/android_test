package com.example.testapplication.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.CharacterGender
import com.example.testapplication.domain.model.FilterCharacters

@Dao
@TypeConverters(Converters::class)
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): PagingSource<Int, Character>

    @Delete
    fun deleteFromFavorite(character: Character)

    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()
}