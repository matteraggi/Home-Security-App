package com.example.homesecurity


sealed class NotBottomBarPages (
    val route: String,
){
    object SingleRecord: NotBottomBarPages(
        route = "singlerecord",
    )

    object WifiList: NotBottomBarPages(
        route = "wifilist",
    )

    object ConnectionWebView: NotBottomBarPages(
        route = "webview",
    )

    object CreateUser: NotBottomBarPages(
        route = "createuser",
    )

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {arg -> append("/$arg") }
        }
    }

}