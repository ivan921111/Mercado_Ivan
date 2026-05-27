package com.example.e_commerce_app.presentation.catalogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.e_commerce_app.domain.model.Producto
import com.example.e_commerce_app.presentation.common.ShimmerProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    viewModel: CatalogoViewModel,
    onNavigateToDetalle: (Int) -> Unit,
    onNavigateToCarrito: () -> Unit
) {
    val productos = viewModel.productosPaginados.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo Móvil", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToCarrito) {
                        Text("🛒")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos.itemCount) { index ->
                    val producto = productos[index]
                    producto?.let {
                        ProductoItem(producto = it) {
                            onNavigateToDetalle(it.id)
                        }
                    }
                }

                // Manejo de estados de carga con Shimmer y Feedback
                productos.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            items(10) { ShimmerProductItem() }
                        }
                        loadState.append is LoadState.Loading -> {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(strokeWidth = 2.dp)
                                }
                            }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val error = productos.loadState.refresh as LoadState.Error
                            item(span = { GridItemSpan(2) }) {
                                ErrorEstado(
                                    mensaje = error.error.localizedMessage ?: "Error de red",
                                    onReintentar = { retry() }
                                )
                            }
                        }
                    }
                }
            }
            
            // Estado vacío
            if (productos.itemCount == 0 && productos.loadState.refresh is LoadState.NotLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos disponibles por ahora.", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun ErrorEstado(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "¡Uy! No pudimos cargar los datos.", color = MaterialTheme.colorScheme.error)
        Text(text = mensaje, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onReintentar) {
            Text("Reintentar")
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, onClick: () -> Unit) {
    val precioFormateado = remember(producto.precioFinal) {
        "$${String.format("%,.0f", producto.precioFinal * 4000)} COP"
    }
    
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = producto.miniatura,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.errorContainer)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = producto.nombre,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = precioFormateado,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
