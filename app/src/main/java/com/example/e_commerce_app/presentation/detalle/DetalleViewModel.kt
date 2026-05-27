package com.example.e_commerce_app.presentation.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.domain.model.Producto
import com.example.e_commerce_app.domain.repository.ProductoRepository
import com.example.e_commerce_app.domain.usecase.GestionarCarritoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val carritoUseCase: GestionarCarritoUseCase
) : ViewModel() {

    private val _producto = MutableStateFlow<Producto?>(null)
    val producto = _producto.asStateFlow()

    fun cargarProducto(id: Int) {
        viewModelScope.launch {
            _producto.value = productoRepository.obtenerProductoPorId(id)
        }
    }

    fun agregarAlCarrito() {
        val p = _producto.value ?: return
        viewModelScope.launch {
            carritoUseCase.agregar(p)
        }
    }
}
