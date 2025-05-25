package edu.ucne.registrotecnicos.presentation.mensaje

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entity.TecnicoEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeScreen(viewModel: MensajesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var tecnicoId by remember { mutableStateOf(uiState.tecnicoId ?: "") }
    var rol by remember { mutableStateOf("Owner") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(uiState.mensajes.reversed()) { mensaje ->
                MensajeCard(mensaje, uiState.tecnicos)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        RolSelector(rol = rol, onRolChange = { rol = it })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = tecnicoId,
            onValueChange = {
                tecnicoId = it
                viewModel.onTecnicoIdChange(it)
            },
            label = { Text("Técnico ID") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = { viewModel.onDescripcionChange(it) },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(uiState.fecha))}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedButton(
            onClick = { viewModel.saveMensaje() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Guardar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar Mensaje")

        }
            Spacer(modifier = Modifier.height(8.dp))

        uiState.successMessage?.let {
            Text(text = it, color = Color(0xFF2E7D32))
        }

        uiState.errorMessage?.let {
            Text(text = it, color = Color(0xFFC62828))
        }
    }
}

@Composable
fun RolSelector(rol: String, onRolChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = rol == "Operator",
            onClick = { onRolChange("Operator") }
        )
        Text("Operator")

        Spacer(modifier = Modifier.width(16.dp))

        RadioButton(
            selected = rol == "Owner",
            onClick = { onRolChange("Owner") }
        )
        Text("Owner")
    }
}

@Composable
fun MensajeCard(mensaje: MensajeEntity, tecnicos: List<TecnicoEntity>) {
    val tecnico = tecnicos.find { it.tecnicoId == mensaje.tecnicoId }
    val isOperator = tecnico?.nombres == "Sandeep" // Lógica temporal: usa tu lógica real aquí
    val chipColor = if (isOperator) Color(0xFF1976D2) else Color(0xFF2E7D32)
    val chipText = if (isOperator) "Operator" else "Owner"
    val alignment = if (isOperator) Alignment.End else Alignment.Start

    val backgroundBrush = if (isOperator) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2)) // Azul oscuro a azul medio
        )
    } else {
        null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(
                    brush = backgroundBrush ?: Brush.verticalGradient(
                        colors = listOf(Color(0xFFE8F5E9), Color(0xFFE8F5E9)) // Mantener fondo verde claro para Owner
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "By ${tecnico?.nombres ?: "Desconocido"}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(chipColor, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = chipText,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = mensaje.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(mensaje.fecha),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}