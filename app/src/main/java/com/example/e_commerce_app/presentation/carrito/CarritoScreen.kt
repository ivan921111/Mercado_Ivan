package com.example.e_commerce_app.presentation.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_commerce_app.domain.model.ItemCarrito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("⬅️")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val s = state) {
                is CarritoState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CarritoState.Vacio -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tu carrito está vacío")
                    }
                }
                is CarritoState.Exito -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(s.resumen.items) { item ->
                                ItemCarritoRow(item = item, onEliminar = { viewModel.eliminar(item.producto.id) })
                            }
                        }
                        ResumenPanel(
                            subtotal = s.resumen.subtotal,
                            descuento = s.resumen.totalDescuento,
                            impuestos = s.resumen.impuestos,
                            total = s.resumen.total
                        )
                    }
                }
                is CarritoState.Error -> {
                    Text("Error: ${s.mensaje}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun ItemCarritoRow(item: ItemCarrito, onEliminar: () -> Unit) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.producto.nombre, fontWeight = FontWeight.Bold)
                Text(text = "Cantidad: ${item.cantidad}")
                Text(
                    text = "$${String.format("%,.0f", item.subtotal * 4000)} COP",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onEliminar) {
                Text("❌")
            }
        }
    }
}

@Composable
fun ResumenPanel(subtotal: Double, descuento: Double, impuestos: Double, total: Double) {
    Surface(tonalElevation = 8.dp) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Subtotal con descuentos:")
                Text("$${String.format("%,.0f", subtotal * 4000)} COP")
            }
            if (descuento > 0) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Ahorro total:", color = MaterialTheme.colorScheme.secondary)
                    Text("-$${String.format("%,.0f", descuento * 4000)} COP", color = MaterialTheme.colorScheme.secondary)
                }
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Impuestos (19% IVA):")
                Text("$${String.format("%,.0f", impuestos * 4000)} COP")
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Total a pagar:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(
                    text = "$${String.format("%,.0f", total * 4000)} COP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* Implementar compra */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Finalizar Compra")
            }
        }
    }
}
