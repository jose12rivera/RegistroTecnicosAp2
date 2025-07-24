package edu.ucne.registrotecnicos.presentation.cliente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entity.ClienteEntity

@Composable
fun ClienteListScreen(
    viewModel: ClienteViewModel = hiltViewModel(),
    goToCliente: (Int) -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getClientes()
    }

    ClienteListBodyScreen(
        uiState = uiState,
        goToCliente = goToCliente,
        onDrawer = onDrawer,
        onRefresh = viewModel::getClientes
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteListBodyScreen(
    uiState: ClienteUiState,
    goToCliente: (Int) -> Unit,
    onDrawer: () -> Unit,
    onRefresh: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                title = { Text("Clientes") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(end = 16.dp)
            ) {
                FloatingActionButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                }
                FloatingActionButton(onClick = { goToCliente(0) }) { // 0 indica NUEVO
                    Icon(Icons.Default.Add, contentDescription = "Agregar cliente")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

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
                    ClienteRow(cliente, goToCliente)
                }
            }
        }
    }
}

@Composable
private fun ClienteRow(
    item: ClienteEntity,
    goToCliente: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { goToCliente(item.clienteId ?: 0) }
            .padding(16.dp)
    ) {
        Text(text = "ID: ${item.clienteId ?: "-"}")
        Text(text = "Nombre: ${item.nombres}")
        Text(text = "WhatsApp: ${item.whatsApp}")
    }
    Divider()
}
