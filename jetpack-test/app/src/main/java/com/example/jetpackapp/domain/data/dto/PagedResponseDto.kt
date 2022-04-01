package com.example.jetpackapp.domain.data.dto

import com.example.jetpackapp.domain.model.PagedResponse
import com.example.jetpackapp.domain.model.Character

data class PagedResponseDto<T>(
    val info: PageInfoDto,
    val results: List<T> = listOf()
) {
    fun toPagedResponseCharacter(): PagedResponse<Character> {
        return PagedResponse(
            pageInfo = info.toPageInfo(),
            results = results.map { (it as CharacterDto).toCharacter() },
        )
    }
}