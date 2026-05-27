package com.example.e_commerce_app.data.mapper

object TraductorHelper {
    private val diccionario = mapOf(
        "beauty" to "Belleza",
        "fragrances" to "Fragancias",
        "furniture" to "Muebles",
        "groceries" to "Abarrotes",
        "Essence Mascara Lash Princess" to "Máscara de Pestañas Essence Lash Princess",
        "Eyeshadow Palette with Mirror" to "Paleta de Sombras con Espejo",
        "Powder Canister" to "Recipiente de Polvo",
        "Red Lipstick" to "Lápiz Labial Rojo",
        "Red Nail Polish" to "Esmalte de Uñas Rojo",
        "Calvin Klein CK One" to "Calvin Klein CK One (Original)",
        "Chanel Coco Noir Eau De" to "Chanel Coco Noir Perfume",
        "Dior J'adore" to "Dior J'adore (Lujo)",
        "Dolce Shine Eau de" to "Dolce Shine Perfume Fresco",
        "Gucci Bloom Eau de" to "Gucci Bloom Fragancia Floral"
    )

    fun traducir(texto: String): String {
        return diccionario[texto] ?: texto
    }

    fun traducirDescripcion(texto: String): String {
        // Traducción simplificada para la demo
        if (texto.contains("mascara")) return "Máscara de pestañas popular conocida por sus efectos de volumen y longitud."
        if (texto.contains("eyeshadow")) return "Paleta de sombras versátil para crear looks impresionantes."
        if (texto.contains("fragrance")) return "Fragancia elegante y sofisticada para cualquier ocasión."
        return "Descripción traducida: $texto"
    }
}
