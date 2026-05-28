# App - MVP Mercado_Ivan

Este proyecto representa un MVP robusto y escalable de una aplicación de E-commerce nativa para Android, desarrollada bajo estándares de nivel producción, con un enfoque total en la **resiliencia**, **rendimiento** y **localización**.

## 🏗️ Arquitectura: Clean Architecture + MVVM

Se ha implementado una arquitectura desacoplada en capas para garantizar la mantenibilidad y facilidad de prueba:

1.  **Capa de Dominio (Domain)**: 
    *   Contiene la lógica de negocio pura en Kotlin.
    *   **Entidades Inmutables**: `Producto` e `ItemCarrito` definidas con `val`.
    *   **Casos de Uso**: `GestionarCarritoUseCase` centraliza los cálculos de IVA (19%), descuentos y totales.
2.  **Capa de Datos (Data)**: 
    *   **Offline-First**: Sincronización automática con **Room (SQLite)**.
    *   **RemoteMediator**: Lógica avanzada para coordinar la red (**Retrofit**) y la base de datos local.
    *   **Mapeadores Inteligentes**: Uso de `@SerialName` para traducir el contrato de la API (Inglés) al código de negocio (Español) sin romper la integridad.
3.  **Capa de Presentación (Presentation)**: 
    *   UI moderna y declarativa con **Jetpack Compose**.
    *   Arquitectura reactiva utilizando **StateFlow** y **SharedFlow**.
    *   **Navegación Type-Safe**: Paso de parámetros blindado por el compilador.

## 💾 Persistencia y Resiliencia

*   **Motor**: Room Database (SQLite). Se eligió por su seguridad en compilación e integración nativa con flujos de datos.
*   **Seguridad de Datos**: Implementación de `fallbackToDestructiveMigration` para actualizaciones de esquema seguras.
*   **Control de Tráfico**: Sistema de **"Cisterna de Skips"** en el `RemoteMediator` para evitar peticiones duplicadas y bucles infinitos de red, garantizando un consumo mínimo de datos y batería.

## ⚡ Rendimiento y UX

*   **Scroll Infinito**: Implementado con **Paging 3** para una carga fluida de productos sin límites manuales.
*   **Búsqueda en Tiempo Real**: Funcionalidad de búsqueda integrada con *Debounce* de 500ms y soporte offline automático.
*   **Imágenes Optimizadas**: Uso de **Coil** con sistema de caché en disco/memoria y *placeholders* visuales elegantes.
*   **Estabilidad de UI**: Uso de `@Immutable` y `remember` en Compose para reducir recomposiciones innecesarias y maximizar los FPS.
*   **Feedback Háptico**: Vibración nativa al interactuar con acciones de conversión (agregar al carrito).

## 🚀 Cómo Ejecutar la App

1.  **Requisitos**: Android Studio Jellyfish+ y SDK 30+ (Android 11).
2.  **Sincronización**: Realizar un *Gradle Sync*.
3.  **Ejecución**: Compilar y ejecutar en emulador o dispositivo real.
4.  **Pruebas**: 
    ```bash
    ./gradlew test
    ```

## 🛠️ Stack Tecnológico
*   **Dagger Hilt**: Inyección de dependencias integral.
*   **Retrofit + OkHttp**: Cliente de red con interceptores de logs y protocolos estables.
*   **Kotlinx Serialization**: Deserialización segura y tolerante.
*   **Jetpack Compose**: UI moderna con componentes de Material 3.

## 📌 Supuestos Técnicos
*   **Moneda**: Se aplica una tasa de conversión base para mostrar precios realistas en **Pesos Colombianos (COP)**.
*   **Idioma**: El código y la interfaz están localizados 100% al **Español**.
*   **Estrategia Offline**: La app prioriza la visualización de datos locales en ausencia de red para evitar interrupciones en la experiencia de compra.

---
