package edu.ucne.registrotecnicos.presentation.mensaje

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entity.TecnicoEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeScreen(viewModel: MensajesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var tecnicoId by remember { mutableStateOf<String>("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (uiState.mensajes.isEmpty()) {
            Text("No hay mensajes disponibles.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.mensajes) { mensaje ->
                    MensajeCard(mensaje, uiState.tecnicos)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = tecnicoId,
            onValueChange = {
                tecnicoId = it
                viewModel.onTecnicoIdChange(it)
            },
            label = { Text("Técnico ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = { viewModel.onDescripcionChange(it) },
            label = { Text("Descripción del Mensaje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(uiState.fecha))}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.saveMensaje() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Mensaje")
        }

        Spacer(modifier = Modifier.height(16.dp))

        uiState.successMessage?.let {
            Text(text = it, color = Color.Green)
        }
        uiState.errorMessage?.let {
            Text(text = it, color = Color.Red)
        }
    }
}


@Composable
fun MensajeCard(mensaje: MensajeEntity, tecnicos: List<TecnicoEntity>) {
    val tecnico = tecnicos.find { it.tecnicoId == mensaje.tecnicoId }

    Column(modifier = Modifier.padding(horizontal = 4.dp)) {
        Text(
            text = "Mensaje enviado por Técnico en ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(mensaje.fecha)}",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Descripción: ${mensaje.descripcion}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(mensaje.fecha)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.Blue)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Técnico",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}