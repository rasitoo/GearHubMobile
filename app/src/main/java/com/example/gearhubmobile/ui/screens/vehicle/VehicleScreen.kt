package com.example.gearhubmobile.ui.screens.vehicle

import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gearhubmobile.data.models.VehicleDetail
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
    val selectedVehicle by viewModel.selectedVehicle.collectAsState()
    var expandedIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadVehicles(userId)
        viewModel.getUser(userId)
        viewModel.getCurrentData()
    }

    Scaffold(
        floatingActionButton = {
            if (viewModel.userId.toString() == viewModel.currentId)
                FloatingActionButton(onClick = {
                    viewModel.setSelectedVehicle(null)
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
                    items(vehicles.size) { index ->
                        val vehicle = vehicles[index]
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        viewModel.setSelectedVehicle(vehicle)
                                        expandedIndex = index
                                    }
                                )
                                .padding(vertical = 8.dp)) {
                            Text(
                                "${vehicle.brand} ${vehicle.model} (${vehicle.year})",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Matrícula: ${vehicle.license}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            DropdownMenu(
                                expanded = expandedIndex == index,
                                onDismissRequest = { expandedIndex = null }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Editar") },
                                    onClick = {
                                        expandedIndex = null
                                        navController.navigate("${Routes.ADD_VEHICLE}/${vehicle.id}")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Eliminar") },
                                    onClick = {
                                        viewModel.deleteVehicle(vehicle.id)
                                        expandedIndex = null
                                    }
                                )
                            }
                            Divider()
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
    navController: NavHostController,
    vehicleId: String? = null
) {
    val vehicle by viewModel.selectedVehicle.collectAsState()

    var vin by rememberSaveable { mutableStateOf(vehicle?.vin ?: "") }
    var brand by rememberSaveable { mutableStateOf(vehicle?.brand ?: "") }
    var model by rememberSaveable { mutableStateOf(vehicle?.model ?: "") }
    var year by rememberSaveable { mutableStateOf(vehicle?.year?.toString() ?: "") }
    var license by rememberSaveable { mutableStateOf(vehicle?.license ?: "") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (vehicleId == null) "Añadir vehículo" else "Editar vehículo") })
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
                        return@Button
                    }
                    if (vehicleId != null) {
                        viewModel.updateVehicle(
                            vehicleId,
                            vin,
                            brand,
                            model,
                            year.toIntOrNull() ?: 0,
                            license
                        )
                    } else {
                        viewModel.createVehicle(vin, brand, model, year.toIntOrNull() ?: 0, license)
                    }
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
