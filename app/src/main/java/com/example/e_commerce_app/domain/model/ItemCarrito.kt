package com.example.e_commerce_app.domain.model

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int
) {
    val subtotal: Double
        get() = producto.precioFinal * cantidad
}
