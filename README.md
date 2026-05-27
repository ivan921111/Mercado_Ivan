# InterCommerce App - MVP E-commerce Robust

Este proyecto es un MVP de una aplicación de E-commerce nativa para Android, desarrollada con un enfoque en **alta ingeniería**, **resiliencia** y **escalabilidad**.

## 🏗️ Arquitectura: Clean Architecture

Se ha implementado **Clean Architecture** con una separación estricta en tres capas para garantizar que la lógica de negocio sea independiente de los frameworks, la UI y la base de datos:

1.  **Capa de Dominio (Domain)**: Contiene las entidades de negocio (`Producto`, `ItemCarrito`) y los Casos de Uso (`GestionarCarritoUseCase`). Es código Kotlin puro, sin dependencias de Android, lo que facilita su testeo y reutilización.
2.  **Capa de Datos (Data)**: Gestiona la persistencia local con **Room** y el consumo de la API de **DummyJSON** con **Retrofit**. Aquí se encuentran los repositorios que deciden la estrategia de obtención de datos.
3.  **Capa de Presentación (Presentation)**: Desarrollada íntegramente con **Jetpack Compose** bajo una arquitectura reactiva. Utiliza **ViewModels** con **StateFlow** para gestionar el estado operacional de la vista.

## 💾 Persistencia y Resiliencia (Offline-First)

### Justificación de Room (SQLite)
Se seleccionó **Room** como motor de persistencia por las siguientes razones:
*   **Seguridad en Compilación**: Room valida las consultas SQL en tiempo de compilación, evitando errores en tiempo de ejecución.
*   **Integración Nativa**: Es el estándar oficial de Google, lo que garantiza soporte a largo plazo y compatibilidad total con **Paging 3** y **Flow**.
*   **Relacional**: Permite estructurar los productos y el carrito de forma relacional, ideal para el volumen transaccional de un catálogo.

### Estrategia de Mitigación de Pérdida de Datos
Para garantizar que los datos sobrevivan a cierres de app o reinicios, se implementó:
*   **RemoteMediator**: Actúa como un coordinador entre la red y la base de datos. Cada producto descargado se guarda inmediatamente en Room. Si la API falla, la app sirve los datos locales de forma transparente.
*   **Persistencia Atómica**: El carrito se gestiona directamente en SQLite, asegurando que cada adición o eliminación sea persistente al instante.

## 🚀 Cómo Ejecutar la App

1.  Clonar el repositorio.
2.  Abrir el proyecto en **Android Studio Jellyfish** (o superior).
3.  Sincronizar Gradle.
4.  Ejecutar en un emulador o dispositivo físico con **Android 11 (API 30)** o superior.

## 🧪 Cómo Correr las Pruebas

Para validar la lógica de negocio (especialmente los cálculos del carrito):
```bash
./gradlew test
```
Esto ejecutará la suite de pruebas unitarias implementadas con **JUnit** y **MockK**.

## 🛠️ Stack Tecnológico
*   **Lenguaje**: Kotlin (Inmutabilidad y Corrutinas).
*   **UI**: Jetpack Compose con Type-Safe Navigation.
*   **DI**: Dagger Hilt.
*   **Red**: Retrofit + OkHttp (Interceptores de Logs).
*   **Imágenes**: Coil (Caché en disco y memoria).
*   **Paginación**: Paging 3 (Scroll infinito).
*   **Localización**: 100% Español y Pesos Colombianos (COP).

## 📌 Supuestos y Limitaciones
*   **Tasa de Cambio**: Se asume una tasa de conversión fija para los precios de la API a COP para fines demostrativos.
*   **Vibración**: Requiere un dispositivo físico con motor háptico para apreciarse plenamente.
*   **Búsqueda**: La búsqueda offline se limita a los productos que ya han sido precargados en la base de datos local.
