package com.example.e_commerce_app.di

import android.content.Context
import androidx.room.Room
import com.example.e_commerce_app.data.local.EcommerceDatabase
import com.example.e_commerce_app.data.local.ProductoDao
import com.example.e_commerce_app.data.remote.ProductoApi
import com.example.e_commerce_app.data.repository.CarritoRepositoryImpl
import com.example.e_commerce_app.data.repository.ProductoRepositoryImpl
import com.example.e_commerce_app.domain.repository.CarritoRepository
import com.example.e_commerce_app.domain.repository.ProductoRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideProductoApi(client: OkHttpClient): ProductoApi {
        val json = Json { 
            ignoreUnknownKeys = true 
            isLenient = true
            coerceInputValues = true
        }
        return Retrofit.Builder()
            .baseUrl(ProductoApi.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ProductoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EcommerceDatabase {
        return Room.databaseBuilder(
            context,
            EcommerceDatabase::class.java,
            "ecommerce_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideProductoDao(db: EcommerceDatabase): ProductoDao = db.dao

    @Provides
    @Singleton
    fun provideProductoRepository(
        api: ProductoApi,
        db: EcommerceDatabase
    ): ProductoRepository = ProductoRepositoryImpl(api, db)

    @Provides
    @Singleton
    fun provideCarritoRepository(
        dao: ProductoDao
    ): CarritoRepository = CarritoRepositoryImpl(dao)
}
