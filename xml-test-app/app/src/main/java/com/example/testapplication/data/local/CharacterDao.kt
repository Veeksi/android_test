package com.example.testapplication.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.CharacterGender
import com.example.testapplication.domain.model.CharacterStatus
import com.example.testapplication.domain.model.FilterCharacters
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(Converters::class)
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Query(
        "SELECT * FROM characters " +
                "WHERE name LIKE '%' || :name || '%' " +
                "AND gender LIKE '%' || :gender || '%' " +
                "AND status LIKE '%' || :status || '%'"
    )
    fun getAllCharacters(name: String, gender: String, status: String): PagingSource<Int, Character>

    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharactersToFavorite(characters: List<Character>)

    @Query("SELECT * FROM characters")
    fun getAllFavoriteCharacters(): Flow<List<Character>>

    @Delete
    suspend fun deleteFromFavorites(characters: List<Character>)
}