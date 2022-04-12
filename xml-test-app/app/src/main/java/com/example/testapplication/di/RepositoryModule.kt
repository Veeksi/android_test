package com.example.testapplication.di

import com.example.testapplication.data.repository.CharacterRepositoryImpl
import com.example.testapplication.data.repository.EpisodeRepositoryImpl
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.domain.repository.EpisodeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCharacterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository

    @Binds
    abstract fun bindEpisodeRepository(
        episodeRepositoryImpl: EpisodeRepositoryImpl
    ): EpisodeRepository
}

/*
Equivalent with this:
@Provides
fun provideCharacterRepository(
    characterRepositoryImpl: CharacterRepositoryImpl
): CharacterRepository {
    return characterRepositoryImpl
}
 */