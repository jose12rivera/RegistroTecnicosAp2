package edu.ucne.registrotecnicos.presentation.cliente


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditClienteScreen(
    viewModel: ClienteViewModel = hiltViewModel(),
    clienteId: Int,
    goBack: () -> Unit
) {
    LaunchedEffect(clienteId) {
        viewModel.selectCliente(clienteId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EditClienteBodyScreen(
        uiState = uiState,
        onNombresChange = viewModel::setNombres,
        onWhatsAppChange = viewModel::setWhatsApp,
        save = viewModel::saveCliente,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClienteBodyScreen(
    uiState: ClienteUiState,
    onNombresChange: (String) -> Unit,
    onWhatsAppChange: (String) -> Unit,
    save: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2)) // degradado azul
                        )
                    )
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Editar Cliente",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = goBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = save) {
                            Icon(Icons.Filled.Check, contentDescription = "Guardar", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
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
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Nombres") },
                value = uiState.nombres ?: "",
                onValueChange = onNombresChange,
                isError = uiState.inputError?.contains("nombre", ignoreCase = true) == true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "WhatsApp") },
                value = uiState.whatsApp ?: "",
                onValueChange = onWhatsAppChange,
                isError = uiState.inputError?.contains("whatsapp", ignoreCase = true) == true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = save
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Guardar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Guardar")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = goBack
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Cancelar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Cancelar")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            uiState.inputError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
