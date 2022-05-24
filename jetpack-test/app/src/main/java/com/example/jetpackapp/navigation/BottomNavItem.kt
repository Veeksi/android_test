package com.example.jetpackapp.navigation

import com.example.jetpackapp.R

sealed class BottomNavItem(var route: String, var icon: Int, var title: String) {
    object Characters : BottomNavItem("characters", R.drawable.ic_home, "Characters")
    object Demos : BottomNavItem("demos", R.drawable.ic_episodes, "Demos")
}
