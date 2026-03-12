package com.example.myapplication

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Detail : Screen("detail/{friendName}") {
        fun createRoute(friendName: String): String = "detail/$friendName"
    }
}