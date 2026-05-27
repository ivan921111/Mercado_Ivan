package com.example.e_commerce_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val porcentajeDescuento: Double,
    val calificacion: Double,
    val stock: Int,
    val marca: String?,
    val categoria: String,
    val miniatura: String,
    val imagenesJson: String
)

@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey val productoId: Int,
    val cantidad: Int
)
