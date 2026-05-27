package com.example.e_commerce_app.domain.usecase

import com.example.e_commerce_app.domain.model.ItemCarrito
import com.example.e_commerce_app.domain.model.Producto
import com.example.e_commerce_app.domain.repository.CarritoRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GestionarCarritoUseCaseTest {

    private val repositorio = mockk<CarritoRepository>()
    private val useCase = GestionarCarritoUseCase(repositorio)

    @Test
    fun `obtenerResumen calcula totales correctamente`() = runBlocking {
        // Given
        val producto1 = Producto(
            id = 1, nombre = "P1", descripcion = "", precio = 100.0,
            porcentajeDescuento = 10.0, calificacion = 4.0, stock = 10, categoria = "",
            miniatura = "", imagenes = emptyList()
        ) // Final price = 90
        val producto2 = Producto(
            id = 2, nombre = "P2", descripcion = "", precio = 200.0,
            porcentajeDescuento = 20.0, calificacion = 4.0, stock = 10, categoria = "",
            miniatura = "", imagenes = emptyList()
        ) // Final price = 160

        val items = listOf(
            ItemCarrito(producto1, 1), // subtotal = 90
            ItemCarrito(producto2, 1)  // subtotal = 160
        )
        every { repositorio.obtenerCarrito() } returns flowOf(items)

        // When
        val resumen = useCase.obtenerResumen().first()

        // Then
        assertEquals(250.0, resumen.subtotal, 0.01)
        assertEquals(250.0 * 0.19, resumen.impuestos, 0.01)
        assertEquals(250.0 * 1.19, resumen.total, 0.01)
    }
}
