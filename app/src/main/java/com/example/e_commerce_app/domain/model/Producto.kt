package com.example.e_commerce_app.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val porcentajeDescuento: Double,
    val calificacion: Double,
    val stock: Int,
    val marca: String? = null,
    val categoria: String,
    val miniatura: String,
    val imagenes: List<String>
) {
    val precioFinal: Double
        get() = precio * (1 - porcentajeDescuento / 100)
}
