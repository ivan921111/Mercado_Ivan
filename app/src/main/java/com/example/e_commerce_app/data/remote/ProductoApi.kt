package com.example.e_commerce_app.data.remote

import com.example.e_commerce_app.data.remote.dto.RespuestaProducto
import com.example.e_commerce_app.data.remote.dto.ProductoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductoApi {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): RespuestaProducto

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductoDto

    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): RespuestaProducto

    companion object {
        const val BASE_URL = "https://dummyjson.com/"
    }
}
