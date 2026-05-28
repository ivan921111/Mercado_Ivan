package com.example.e_commerce_app.data.local

import androidx.room.*
import com.example.e_commerce_app.data.local.entity.CarritoEntity
import com.example.e_commerce_app.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    // Productos
    @Query("SELECT * FROM productos")
    fun getAllProductsPaged(): androidx.paging.PagingSource<Int, ProductoEntity>

    @Query("SELECT * FROM productos")
    fun getAllProducts(): Flow<List<ProductoEntity>>

    @Query("DELETE FROM productos")
    suspend fun clearAllProducts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductoEntity>)

    @Query("SELECT * FROM productos WHERE titulo LIKE '%' || :query || '%'")
    fun buscarProductosPaged(query: String): androidx.paging.PagingSource<Int, ProductoEntity>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductById(id: Int): ProductoEntity?

    @Query("SELECT * FROM productos WHERE titulo LIKE '%' || :query || '%'")
    suspend fun buscarProductos(query: String): List<ProductoEntity>

    // Carrito
    @Query("SELECT * FROM carrito")
    fun getCarritoItems(): Flow<List<CarritoEntity>>

    @Query("SELECT * FROM carrito WHERE productoId = :id")
    suspend fun getCarritoItemById(id: Int): CarritoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarritoItem(item: CarritoEntity)

    @Query("DELETE FROM carrito WHERE productoId = :id")
    suspend fun deleteCarritoItem(id: Int)

    @Query("DELETE FROM carrito")
    suspend fun clearCarrito()
}
