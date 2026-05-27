package com.example.e_commerce_app.data.repository

import com.example.e_commerce_app.data.local.ProductoDao
import com.example.e_commerce_app.data.local.entity.CarritoEntity
import com.example.e_commerce_app.data.mapper.toDomain
import com.example.e_commerce_app.domain.model.ItemCarrito
import com.example.e_commerce_app.domain.model.Producto
import com.example.e_commerce_app.domain.repository.CarritoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarritoRepositoryImpl @Inject constructor(
    private val dao: ProductoDao
) : CarritoRepository {

    override fun obtenerCarrito(): Flow<List<ItemCarrito>> {
        return combine(
            dao.getCarritoItems(),
            dao.getAllProducts()
        ) { carritoEntities, productosEntities ->
            carritoEntities.mapNotNull { carrito ->
                val producto = productosEntities.find { it.id == carrito.productoId }
                producto?.let {
                    ItemCarrito(
                        producto = it.toDomain(),
                        cantidad = carrito.cantidad
                    )
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun agregarAlCarrito(producto: Producto) = withContext(Dispatchers.IO) {
        val existente = dao.getCarritoItemById(producto.id)
        if (existente != null) {
            dao.insertCarritoItem(existente.copy(cantidad = existente.cantidad + 1))
        } else {
            dao.insertCarritoItem(
                CarritoEntity(
                    productoId = producto.id,
                    cantidad = 1
                )
            )
        }
    }

    override suspend fun eliminarDelCarrito(productoId: Int) = withContext(Dispatchers.IO) {
        dao.deleteCarritoItem(productoId)
    }

    override suspend fun limpiarCarrito() = withContext(Dispatchers.IO) {
        dao.clearCarrito()
    }
}
