package com.example.e_commerce_app.domain.repository

import com.example.e_commerce_app.domain.model.ItemCarrito
import com.example.e_commerce_app.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {
    fun obtenerCarrito(): Flow<List<ItemCarrito>>
    suspend fun agregarAlCarrito(producto: Producto)
    suspend fun eliminarDelCarrito(productoId: Int)
    suspend fun limpiarCarrito()
}
