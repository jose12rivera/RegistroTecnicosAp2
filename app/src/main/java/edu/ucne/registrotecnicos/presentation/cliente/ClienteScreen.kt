package edu.ucne.registrotecnicos.presentation.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ClienteScreen(
    clienteId: Int,
    viewModel: ClienteViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val clienteGuardado = viewModel.clienteGuardado.collectAsStateWithLifecycle().value

    LaunchedEffect(clienteId) {
        if (clienteId != 0) {
            viewModel.selectCliente(clienteId)
        } else {
            viewModel.limpiarCampos()
        }
    }

    // 👉 Navega atrás automáticamente si el cliente fue guardado
    LaunchedEffect(clienteGuardado) {
        if (clienteGuardado) {
            viewModel.resetClienteGuardado()
            goBack()
        }
    }

    ClienteBodyScreen(
        uiState = uiState,
        onNombresChange = viewModel::setNombres,
        onWhatsAppChange = viewModel::setWhatsApp,
        save = viewModel::saveCliente,
        nuevo = viewModel::limpiarCampos,
        goBack = goBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteBodyScreen(
    uiState: ClienteUiState,
    onNombresChange: (String) -> Unit,
    onWhatsAppChange: (String) -> Unit,
    save: () -> Unit,
    nuevo: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                    )
                )
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Registrar Cliente",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = goBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.nombres ?: "",
                onValueChange = onNombresChange,
                label = { Text("Nombres") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.whatsApp ?: "",
                onValueChange = onWhatsAppChange,
                label = { Text("WhatsApp") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = save,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                    Icon(Icons.Filled.Check, contentDescription = null)
                }

                OutlinedButton(
                    onClick = nuevo,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Nuevo")
                    Icon(Icons.Filled.Refresh, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            uiState.inputError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
