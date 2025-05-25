package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
fun TicketScreen(viewModel: TicketViewModel = hiltViewModel(), goBack: () -> Unit, goToMensajeScreen: () -> Unit) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TicketBodyScreen(
        uiState = uiState.value,
        onFechaChange = viewModel::onFechaChange,
        onClienteChange = viewModel::onClienteChange,
        onAsuntoChange = viewModel::onAsuntoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onTecnicoIdChange = viewModel::onTecnicoIdChange,
        onPrioridadIdChange = viewModel::onPrioridadIdChange,
        saveTicket = viewModel::saveTicket,
        nuevoTicket = viewModel::nuevoTicket,
        goBack = goBack,
        goToMensajeScreen = goToMensajeScreen
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyScreen(
    uiState: TicketViewModel.UiState,
    onFechaChange: (String) -> Unit,
    onClienteChange: (String) -> Unit,
    onAsuntoChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onTecnicoIdChange: (Int?) -> Unit,
    onPrioridadIdChange: (Int?) -> Unit,
    saveTicket: () -> Unit,
    nuevoTicket: () -> Unit,
    goToMensajeScreen: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                        )
                    )
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Registrar Ticket",
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
                .verticalScroll(rememberScrollState())
        ) {
            // Mensajes de error o éxito
            if (!uiState.mensajeError.isNullOrEmpty()) {
                Text(
                    text = uiState.mensajeError,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (!uiState.mensajeExito.isNullOrEmpty()) {
                Text(
                    text = uiState.mensajeExito,
                    color = Color(0xFF388E3C),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Fecha") },
                value = uiState.fecha,
                onValueChange = onFechaChange,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Prioridad ID") },
                value = uiState.prioridadId?.toString() ?: "",
                onValueChange = {
                    // Solo aceptar números o vacío
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        onPrioridadIdChange(it.toIntOrNull())
                    }
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Cliente") },
                value = uiState.cliente,
                onValueChange = onClienteChange,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Asunto") },
                value = uiState.asunto,
                onValueChange = onAsuntoChange,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange,
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Técnico ID") },
                value = uiState.tecnicoId?.toString() ?: "",
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        onTecnicoIdChange(it.toIntOrNull())
                    }
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { nuevoTicket() }
                ) {
                    Text(text = "Nuevo")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Filled.Refresh, contentDescription = "Nuevo")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { saveTicket() }
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Guardar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar")
                }
            }
        }
    }
}
