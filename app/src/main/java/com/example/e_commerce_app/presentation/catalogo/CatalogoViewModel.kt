package com.example.e_commerce_app.presentation.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.e_commerce_app.domain.model.Producto
import com.example.e_commerce_app.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CatalogoViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _consulta = MutableStateFlow("")
    val consulta = _consulta.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val productosPaginados: Flow<PagingData<Producto>> = _consulta
        .debounce(500) // Esperamos a que el usuario termine de escribir
        .flatMapLatest { consulta ->
            repository.obtenerProductosPaginados(consulta)
        }
        .cachedIn(viewModelScope)

    fun buscar(nuevaConsulta: String) {
        _consulta.value = nuevaConsulta
    }
}
