package com.example.e_commerce_app.presentation.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.e_commerce_app.domain.model.Producto
import com.example.e_commerce_app.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CatalogoViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    val productosPaginados: Flow<PagingData<Producto>> = repository
        .obtenerProductosPaginados()
        .cachedIn(viewModelScope)
}
