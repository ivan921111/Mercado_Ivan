package com.example.e_commerce_app.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.e_commerce_app.data.local.EcommerceDatabase
import com.example.e_commerce_app.data.local.entity.ProductoEntity
import com.example.e_commerce_app.data.mapper.toEntity
import com.example.e_commerce_app.data.remote.ProductoApi

@OptIn(ExperimentalPagingApi::class)
class ProductoRemoteMediator(
    private val db: EcommerceDatabase,
    private val api: ProductoApi,
    private val consulta: String = "" // Nueva variable de búsqueda
) : RemoteMediator<Int, ProductoEntity>() {

    private val skipsCargados = mutableSetOf<Int>()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductoEntity>
    ): MediatorResult {
        return try {
            val skip = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val count = state.pages.sumOf { it.data.size }
                    if (count == 0) return MediatorResult.Success(endOfPaginationReached = false)
                    count
                }
            }

            if (skipsCargados.contains(skip) && loadType != LoadType.REFRESH) {
                return MediatorResult.Success(endOfPaginationReached = false)
            }

            // LÓGICA DE BÚSQUEDA DINÁMICA
            val respuesta = if (consulta.isBlank()) {
                api.getProducts(limit = state.config.pageSize, skip = skip)
            } else {
                api.searchProducts(query = consulta) // DummyJSON search no pagina igual, pero la API lo soporta
            }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.dao.clearAllProducts()
                    skipsCargados.clear()
                }
                
                val entidades = respuesta.productos.map { it.toEntity() }
                db.dao.insertProducts(entidades)
                skipsCargados.add(skip)
            }

            MediatorResult.Success(
                // Si hay una búsqueda activa, DummyJSON suele mandar todo de golpe
                endOfPaginationReached = respuesta.productos.isEmpty() || consulta.isNotBlank()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
