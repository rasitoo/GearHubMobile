package com.example.gearhubmobile.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gearhubmobile.ui.screens.InitScreen

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navController: NavHostController,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = viewModel.isLoading
    val loginSuccess = viewModel.loginSuccess

    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                navController.navigate(InitScreen.Register.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No estás registrado?")
        }

        TextButton(
            onClick = {
                navController.navigate(InitScreen.Recover.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Olvidaste tu contraseña?")
        }

        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            loginSuccess == false -> {
                Text("Credenciales incorrectas o error de conexión", color = Color.Red)
            }
        }
    }
}

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isWorkshop by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val isLoading = viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isWorkshop,
                onCheckedChange = { isWorkshop = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("¿Eres un taller?")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (password != confirmPassword) {
                    error = "Las contraseñas no coinciden"
                } else {
                    error = null
                    viewModel.register(name, password, confirmPassword, email, isWorkshop)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }

        when {
            isLoading -> {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            error != null -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(error!!, color = Color.Red)
            }
        }
    }
}

@Composable
fun RecoverScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    val isLoading = viewModel.isLoading
    var error by remember { mutableStateOf<String?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Rcuperar contraseña", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                error = null
                viewModel.recover(email)
                navController.popBackStack()

            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Recuperar")
        }
        when {
            isLoading -> {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            error != null -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(error!!, color = Color.Red)
            }
        }
    }
}
