package com.example.e_commerce_app.presentation.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce_app.domain.usecase.GestionarCarritoUseCase
import com.example.e_commerce_app.domain.usecase.ResumenCarrito
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val useCase: GestionarCarritoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CarritoState>(CarritoState.Cargando)
    val state = _state.asStateFlow()

    init {
        observarCarrito()
    }

    private fun observarCarrito() {
        useCase.obtenerResumen()
            .onEach { resumen ->
                _state.value = if (resumen.items.isEmpty()) {
                    CarritoState.Vacio
                } else {
                    CarritoState.Exito(resumen)
                }
            }
            .catch { e ->
                _state.value = CarritoState.Error(e.localizedMessage ?: "Error desconocido")
            }
            .launchIn(viewModelScope)
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            useCase.eliminar(id)
        }
    }
}

sealed interface CarritoState {
    data object Cargando : CarritoState
    data object Vacio : CarritoState
    data class Exito(val resumen: ResumenCarrito) : CarritoState
    data class Error(val mensaje: String) : CarritoState
}
