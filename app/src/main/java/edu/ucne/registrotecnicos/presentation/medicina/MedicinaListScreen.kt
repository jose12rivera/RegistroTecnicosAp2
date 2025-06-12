package edu.ucne.registrotecnicos.presentation.medicina


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.remote.dto.MedicinasDto

@Composable
fun MedicinaListScreen(
    viewModel: MedicinaViewModel = hiltViewModel(),
    goToMedicina: (Int) -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MedicinaListBodyScreen(
        uiState = uiState,
        goToMedicina = goToMedicina,
        onDrawer = onDrawer,
        onRefresh = viewModel::getMedicinas
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinaListBodyScreen(
    uiState: MedicinaUiState,
    goToMedicina: (Int) -> Unit,
    onDrawer: () -> Unit,
    onRefresh: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(
                title = { Text("Medicinas") },
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
                FloatingActionButton(onClick = { goToMedicina(0) }) { // 0 o -1 indica NUEVO
                    Icon(Icons.Default.Add, contentDescription = "Agregar medicina")
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
                items(uiState.medicinas) { medicina ->
                    MedicinaRow(medicina, goToMedicina)
                }
            }
        }
    }
}

@Composable
private fun MedicinaRow(
    item: MedicinasDto,
    goToMedicina: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { goToMedicina(item.medicinaId) }
            .padding(16.dp)
    ) {
        Text(text = "ID: ${item.medicinaId}")
        Text(text = "Descripción: ${item.descripcion ?: "Sin descripción"}")
        Text(text = "Monto: RD$${item.monto}")
    }
    Divider()
}
