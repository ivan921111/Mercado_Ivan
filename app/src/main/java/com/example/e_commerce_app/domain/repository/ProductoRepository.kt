package com.example.e_commerce_app.domain.repository

import androidx.paging.PagingData
import com.example.e_commerce_app.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun obtenerProductosPaginados(consulta: String = ""): Flow<PagingData<Producto>>
    suspend fun obtenerProductoPorId(id: Int): Producto?
}
