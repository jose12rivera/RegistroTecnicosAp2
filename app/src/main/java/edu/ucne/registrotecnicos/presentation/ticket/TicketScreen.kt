package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Fecha") },
                value = uiState.fecha,
                onValueChange = onFechaChange
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Prioridad ID") },
                value = uiState.prioridadId?.toString() ?: "",
                onValueChange = { onPrioridadIdChange(it.toIntOrNull()) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Cliente") },
                value = uiState.cliente,
                onValueChange = onClienteChange
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Asunto") },
                value = uiState.asunto,
                onValueChange = onAsuntoChange
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Técnico ID") },
                value = uiState.tecnicoId?.toString() ?: "",
                onValueChange = { onTecnicoIdChange(it.toIntOrNull()) }
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
                    Icon(Icons.Filled.Refresh, contentDescription = "Nuevo")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { saveTicket() }
                ) {
                    Text(text = "Guardar")
                    Icon(Icons.Filled.Add, contentDescription = "Guardar")
                }
            }

//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedButton(
//                modifier = Modifier.fillMaxWidth(),
//                onClick = goToMensajeScreen
//            ) {
//                Text(text = "Enviar Mensajes")
//                Icon(Icons.Filled.Email, contentDescription = "Enviar Mensajes")
//            }
//
//            uiState.mensajeError?.let { message ->
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = message,
//                    color = Color.Red,
//                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                )
//            }
        }
    }
}