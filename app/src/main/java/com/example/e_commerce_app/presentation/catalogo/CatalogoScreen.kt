package com.example.e_commerce_app.presentation.catalogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    val consulta by viewModel.consulta.collectAsState()

    Scaffold(
        topBar = {
            Surface(tonalElevation = 4.dp) {
                Column {
                    CenterAlignedTopAppBar(
                        title = { Text("Catálogo Móvil", fontWeight = FontWeight.Bold) },
                        actions = {
                            IconButton(onClick = onNavigateToCarrito) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    OutlinedTextField(
                        value = consulta,
                        onValueChange = { viewModel.buscar(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                        placeholder = { Text("¿Qué estás buscando?") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        // Eliminamos el Box extra y usamos el padding directamente en la grilla para mejor control
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 16.dp,
                start = 8.dp,
                end = 8.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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

            // Estados de carga integrados en la grilla
            productos.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        items(10) { ShimmerProductItem() }
                    }
                    loadState.append is LoadState.Loading -> {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
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

        // Estado vacío flotante para no interferir con la grilla
        if (productos.itemCount == 0 && productos.loadState.refresh is LoadState.NotLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontraron resultados para '$consulta'",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ErrorEstado(mensaje: String, onReintentar: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Problema al cargar datos", fontWeight = FontWeight.Bold)
            Text(text = mensaje, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onReintentar) { Text("Reintentar Carga") }
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
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            AsyncImage(
                model = producto.miniatura,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.errorContainer)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = producto.nombre,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    minLines = 2 // Mantiene altura constante en los títulos
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = precioFormateado,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
