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
    private val api: ProductoApi
) : RemoteMediator<Int, ProductoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductoEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        0
                    } else {
                        state.pages.sumOf { it.data.size }
                    }
                }
            }

            val respuesta = api.getProducts(
                limit = state.config.pageSize,
                skip = loadKey
            )

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.dao.clearAllProducts()
                }
                val entidades = respuesta.productos.map { it.toEntity() }
                db.dao.insertProducts(entidades)
            }

            MediatorResult.Success(
                endOfPaginationReached = respuesta.productos.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
