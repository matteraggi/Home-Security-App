package com.example.homesecurity


sealed class NotBottomBarPages (
    val route: String,
    val title: String,
){
    object SingleRecord: NotBottomBarPages(
        route = "singlerecord",
        title = "Single Record"
    )

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg -> append("/$arg") }
        }
    }

}