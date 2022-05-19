package com.example.testapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.RemoteKeys
import dagger.hilt.android.qualifiers.ApplicationContext


@Database(
    entities = [Character::class, RemoteKeys::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharacterDao
    abstract fun charactersRemoteKeyDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: CharacterDatabase? = null

        fun getInstance(@ApplicationContext context: Context): CharacterDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        CharacterDatabase::class.java,
                        "character_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}