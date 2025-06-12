package edu.ucne.registrotecnicos.presentation.medicina


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun MedicinaScreen(
    medicinaId: Int = 0,
    viewModel: MedicinaViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    LaunchedEffect(medicinaId) {
        if (medicinaId != 0) { // Solo cargar si ID válido
            viewModel.limpiarCampos() // Si es nuevo registro
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    MedicinaBodyScreen(
        uiState = uiState.value,
        onDescripcionChange = { viewModel.setDescripcion(it) },
        onMontoChange = { viewModel.setMonto(it.toDoubleOrNull() ?: 0.0) },
        saveMedicina = {
            if (viewModel.validarCampos()) {
                viewModel.update()
                goBack()  // Volver atrás después de guardar
            }
        },
        nuevoMedicina = { viewModel.limpiarCampos() },
        goBack = goBack
    )
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinaBodyScreen(
    uiState: MedicinaUiState,
    onDescripcionChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    saveMedicina: () -> Unit,
    nuevoMedicina: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier.background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                    )
                )
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Registrar Medicina",
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
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Descripción") },
                value = uiState.descripcion ?: "",
                onValueChange = onDescripcionChange,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Monto") },
                value = uiState.monto.toString(),
                onValueChange = {
                    val parsed = it.toDoubleOrNull()
                    if (parsed != null) onMontoChange(it)
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { nuevoMedicina() }
                ) {
                    Text("Nuevo")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Filled.Refresh, contentDescription = "Nuevo")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { saveMedicina() }
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Guardar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar")
                }
            }
        }
    }
}
