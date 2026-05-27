package com.example.e_commerce_app.domain.usecase

import com.example.e_commerce_app.domain.model.ItemCarrito
import com.example.e_commerce_app.domain.model.Producto
import com.example.e_commerce_app.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ResumenCarrito(
    val items: List<ItemCarrito>,
    val subtotal: Double,
    val totalDescuento: Double,
    val impuestos: Double,
    val total: Double
)

class GestionarCarritoUseCase @Inject constructor(
    private val repositorio: CarritoRepository
) {
    fun obtenerResumen(): Flow<ResumenCarrito> {
        return repositorio.obtenerCarrito().map { items ->
            // Lógica de Negocio pura en Domain
            val precioBaseTotal = items.sumOf { it.producto.precio * it.cantidad }
            val subtotalConDescuento = items.sumOf { it.subtotal }
            val ahorroTotal = precioBaseTotal - subtotalConDescuento
            
            val impuestosCalculados = subtotalConDescuento * 0.19 // 19% IVA Colombia
            
            ResumenCarrito(
                items = items,
                subtotal = subtotalConDescuento,
                totalDescuento = ahorroTotal,
                impuestos = impuestosCalculados,
                total = subtotalConDescuento + impuestosCalculados
            )
        }
    }

    suspend fun agregar(producto: Producto) = repositorio.agregarAlCarrito(producto)
    suspend fun eliminar(productoId: Int) = repositorio.eliminarDelCarrito(productoId)
}
