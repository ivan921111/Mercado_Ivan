package com.example.e_commerce_app.data.repository

import androidx.paging.*
import com.example.e_commerce_app.data.local.EcommerceDatabase
import com.example.e_commerce_app.data.mapper.toDomain
import com.example.e_commerce_app.data.remote.ProductoApi
import com.example.e_commerce_app.domain.model.Producto
import com.example.e_commerce_app.domain.repository.ProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductoRepositoryImpl @Inject constructor(
    private val api: ProductoApi,
    private val db: EcommerceDatabase
) : ProductoRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun obtenerProductosPaginados(consulta: String): Flow<PagingData<Producto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            remoteMediator = ProductoRemoteMediator(db, api, consulta),
            pagingSourceFactory = { 
                if (consulta.isBlank()) {
                    db.dao.getAllProductsPaged()
                } else {
                    db.dao.buscarProductosPaged(consulta)
                }
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun obtenerProductoPorId(id: Int): Producto? = withContext(Dispatchers.IO) {
        val local = db.dao.getProductById(id)
        if (local != null) return@withContext local.toDomain()

        try {
            api.getProductById(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }
}
