package com.example.testapplication.data.dto

import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.PagedResponse

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