package com.example.testapplication.domain.model

import com.google.gson.annotations.SerializedName

data class PagedResponse<T>(
    @SerializedName("info")
    val pageInfo: PageInfo,
    val results: List<T> = listOf()
)