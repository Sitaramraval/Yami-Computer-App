package com.example.yamicomputer.navigation

sealed class Routes(val id: String) {

    object SplashScreen: Routes("splash_screen")
    object LoginScreen: Routes("login_screen")
    object HomeScreen: Routes("home_screen")
    object ProfileCreateScreen: Routes("profile_create_screen")
    object AddComplaintScreen: Routes("add_complaint_screen")
    object TotalComplaintScreen: Routes("total_complaint_screen")
    object AddProductScreen: Routes("add_products_screen")
    object AllProductsScreen: Routes("all_products_screen")

}