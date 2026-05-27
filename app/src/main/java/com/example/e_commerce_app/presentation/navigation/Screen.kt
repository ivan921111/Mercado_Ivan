package com.example.e_commerce_app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Catalogo : Screen

    @Serializable
    data class Detalle(val productoId: Int) : Screen

    @Serializable
    data object Carrito : Screen
}
