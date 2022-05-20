package com.example.testapplication.domain.model

import com.example.testapplication.R

data class NavigationItem(
    val title: String,
    val description: String,
    val action: Int
)

val navigationItems = arrayListOf(
    NavigationItem(
        "Example",
        "Describes how to implement collapsible toolbar",
        5,
    ),
    NavigationItem(
        "Collapsible toolbar",
        "Describes how to implement collapsible toolbar",
        R.id.action_otherFragment_to_testFragment
    ),
    NavigationItem(
        "Carousel RecyclerView",
        "Describes how to implement carousel recyclerView",
        R.id.action_otherFragment_to_carouselRecyclerViewFragment
    ),
)