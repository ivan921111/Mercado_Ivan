package com.example.e_commerce_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.e_commerce_app.presentation.carrito.CarritoScreen
import com.example.e_commerce_app.presentation.carrito.CarritoViewModel
import com.example.e_commerce_app.presentation.catalogo.CatalogoScreen
import com.example.e_commerce_app.presentation.catalogo.CatalogoViewModel
import com.example.e_commerce_app.presentation.detalle.DetalleScreen
import com.example.e_commerce_app.presentation.detalle.DetalleViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Catalogo
    ) {
        composable<Screen.Catalogo> {
            val viewModel: CatalogoViewModel = hiltViewModel()
            CatalogoScreen(
                viewModel = viewModel,
                onNavigateToDetalle = { id ->
                    navController.navigate(Screen.Detalle(id))
                },
                onNavigateToCarrito = {
                    navController.navigate(Screen.Carrito)
                }
            )
        }
        composable<Screen.Detalle> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Detalle>()
            val viewModel: DetalleViewModel = hiltViewModel()
            DetalleScreen(
                productoId = args.productoId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Carrito> {
            val viewModel: CarritoViewModel = hiltViewModel()
            CarritoScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
