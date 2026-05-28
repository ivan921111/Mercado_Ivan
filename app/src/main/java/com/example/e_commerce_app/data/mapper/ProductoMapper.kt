package com.example.e_commerce_app.data.mapper

import com.example.e_commerce_app.data.local.entity.ProductoEntity
import com.example.e_commerce_app.data.remote.dto.ProductoDto
import com.example.e_commerce_app.domain.model.Producto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Instancia única configurada para evitar fallos de llaves desconocidas o nulos
private val jsonConfig = Json { 
    ignoreUnknownKeys = true 
    isLenient = true
    encodeDefaults = true
}

fun ProductoDto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = id,
        titulo = TraductorHelper.traducir(tituloOriginal),
        descripcion = descripcionOriginal,
        precio = precioOriginal,
        porcentajeDescuento = porcentajeDescuento,
        calificacion = calificacion,
        stock = stock,
        marca = marca,
        categoria = TraductorHelper.traducir(categoriaOriginal),
        miniatura = miniatura,
        imagenesJson = jsonConfig.encodeToString(imagenes)
    )
}

fun ProductoEntity.toDomain(): Producto {
    return Producto(
        id = id,
        nombre = titulo,
        descripcion = descripcion,
        precio = precio,
        porcentajeDescuento = porcentajeDescuento,
        calificacion = calificacion,
        stock = stock,
        marca = marca,
        categoria = categoria,
        miniatura = miniatura,
        imagenes = try { 
            jsonConfig.decodeFromString(imagenesJson) 
        } catch (e: Exception) { 
            emptyList() 
        }
    )
}

fun ProductoDto.toDomain(): Producto {
    return Producto(
        id = id,
        nombre = TraductorHelper.traducir(tituloOriginal),
        descripcion = descripcionOriginal,
        precio = precioOriginal,
        porcentajeDescuento = porcentajeDescuento,
        calificacion = calificacion,
        stock = stock,
        marca = marca,
        categoria = TraductorHelper.traducir(categoriaOriginal),
        miniatura = miniatura,
        imagenes = imagenes
    )
}
