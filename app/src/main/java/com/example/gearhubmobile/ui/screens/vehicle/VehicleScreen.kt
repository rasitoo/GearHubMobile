package com.example.gearhubmobile.ui.screens.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gearhubmobile.ui.navigation.Routes

/**
 * @author Rodrigo
 * @date 14 junio, 2025
 */
@Composable
fun VehiclesScreen(
    userId: String?,
    viewModel: VehicleViewModel,
    navController: NavHostController
) {
    val vehicles = viewModel.vehicles

    LaunchedEffect(Unit) {
        viewModel.loadVehicles(userId)
        viewModel.getUser(userId)
        viewModel.getCurrentData()
    }
    Scaffold(
        floatingActionButton = {
            if (viewModel.userId.toString() == viewModel.currentId)
                FloatingActionButton(onClick = {
                    navController.navigate(Routes.ADD_VEHICLE)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir coche")
                }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            Text(
                "Vehículos de ${viewModel.user?.userName}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (vehicles.isEmpty()) {
                Text("No tienes coches añadidos.")
            } else {
                LazyColumn {
                    vehicles.forEach { vehicle ->
                        item()
                        {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    "${vehicle.brand} ${vehicle.model} (${vehicle.year})",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    "Matrícula: ${vehicle.license}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(
    viewModel: VehicleViewModel,
    navController: NavHostController
) {
    var vin by rememberSaveable { mutableStateOf("") }
    var brand by rememberSaveable { mutableStateOf("") }
    var model by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var license by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Añadir vehículo") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = vin,
                onValueChange = { vin = it },
                label = { Text("Nº de bastidor") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Modelo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = year,
                onValueChange = { year = it.filter { c -> c.isDigit() } },
                label = { Text("Año") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = license,
                onValueChange = { license = it },
                label = { Text("Matrícula") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (brand.isBlank() || model.isBlank() || year.isBlank() || license.isBlank()) {
                        error = "Todos los campos son obligatorios"
                    }
                    viewModel.createVehicle(vin, brand, model, year.toIntOrNull() ?: 0, license)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar vehículo")
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(error!!, color = Color.Red)
            }
        }
    }
}
