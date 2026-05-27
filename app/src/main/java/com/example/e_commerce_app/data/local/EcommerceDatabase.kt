package com.example.e_commerce_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_commerce_app.data.local.entity.CarritoEntity
import com.example.e_commerce_app.data.local.entity.ProductoEntity

@Database(
    entities = [ProductoEntity::class, CarritoEntity::class],
    version = 2, // Incrementado de 1 a 2 por cambio de esquema a español
    exportSchema = false
)
abstract class EcommerceDatabase : RoomDatabase() {
    abstract val dao: ProductoDao
}
