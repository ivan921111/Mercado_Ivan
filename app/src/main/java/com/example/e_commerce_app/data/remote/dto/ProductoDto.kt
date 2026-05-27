package com.example.e_commerce_app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductoDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val tituloOriginal: String,
    @SerialName("description") val descripcionOriginal: String,
    @SerialName("price") val precioOriginal: Double,
    @SerialName("discountPercentage") val porcentajeDescuento: Double,
    @SerialName("rating") val calificacion: Double,
    @SerialName("stock") val stock: Int,
    @SerialName("brand") val marca: String? = null,
    @SerialName("category") val categoriaOriginal: String,
    @SerialName("thumbnail") val miniatura: String,
    @SerialName("images") val imagenes: List<String>
)

@Serializable
data class RespuestaProducto(
    @SerialName("products") val productos: List<ProductoDto>,
    @SerialName("total") val total: Int,
    @SerialName("skip") val skip: Int,
    @SerialName("limit") val limit: Int
)
