package edu.ucne.registrotecnicos.presentation.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entity.ClienteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ClienteListScreen(
    viewModel: ClienteViewModel = hiltViewModel(),
    drawerState: DrawerState,
    scope: CoroutineScope,
    createCliente: () -> Unit,
    onEditCliente: (Int) -> Unit,
    onDeleteCliente: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getClientes()
    }

    ClienteListBodyScreen(
        uiState = uiState,
        onDrawer = {
            scope.launch { drawerState.open() }
        },
        onRefresh = viewModel::getClientes,
        createCliente = createCliente,
        onEditCliente = onEditCliente,
        onDeleteCliente = onDeleteCliente
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteListBodyScreen(
    uiState: ClienteUiState,
    onDrawer: () -> Unit,
    onRefresh: () -> Unit,
    createCliente: () -> Unit,
    onEditCliente: (Int) -> Unit,
    onDeleteCliente: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2)) // Fondo azul
                )
            ),
        topBar = {
            MediumTopAppBar(
                title = { Text("Clientes") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createCliente,
                containerColor = Color(0xFFD32F2F) // Rojo igual que en las OptionCard
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar cliente", tint = Color.White)
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.clientes) { cliente ->
                    ClienteRow(cliente, onEditCliente, onDeleteCliente)
                }
            }
        }
    }
}

@Composable
private fun ClienteRow(
    item: ClienteEntity,
    onEditCliente: (Int) -> Unit,
    onDeleteCliente: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "ID: ${item.clienteId ?: "-"}")
            Text(text = "Nombre: ${item.nombres}")
            Text(text = "WhatsApp: ${item.whatsApp}")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = { onEditCliente(item.clienteId ?: 0) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = { onDeleteCliente(item.clienteId ?: 0) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
    Divider()
}
