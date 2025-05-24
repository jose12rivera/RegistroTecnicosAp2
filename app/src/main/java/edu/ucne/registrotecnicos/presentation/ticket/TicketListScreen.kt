package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.registrotecnicos.R
import edu.ucne.registrotecnicos.data.local.entity.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TicketListScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,  // Recibe NavController para navegación
    viewModel: TicketViewModel = hiltViewModel(),
    createTicket: () -> Unit,
    onEditTicket: (TicketEntity) -> Unit,
    onDeleteTicket: (TicketEntity) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tecnicoList = uiState.tecnicos

    var searchQuery by remember { mutableStateOf("") }

    val filteredTickets = uiState.tickets.filter {
        it.asunto.contains(searchQuery, ignoreCase = true) ||
                it.cliente.contains(searchQuery, ignoreCase = true)
    }

    TicketListBodyScreen(
        drawerState = drawerState,
        scope = scope,
        uiState = uiState,
        createTicket = createTicket,
        onEditTicket = onEditTicket,
        onDeleteTicket = onDeleteTicket,
        tecnicoList = tecnicoList,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        filteredTickets = filteredTickets,
        onMessageTicket = { ticket ->
            // Navegar a la pantalla de mensajes con el ID del técnico
            navController.navigate("mensaje/${ticket.tecnicoId}")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListBodyScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    uiState: TicketViewModel.UiState,
    createTicket: () -> Unit,
    onEditTicket: (TicketEntity) -> Unit,
    onDeleteTicket: (TicketEntity) -> Unit,
    tecnicoList: List<TecnicoEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    filteredTickets: List<TicketEntity>,
    onMessageTicket: (TicketEntity) -> Unit // Parámetro para manejar mensajes
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Tickets",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Image(
                            painter = painterResource(id = R.drawable.tic),
                            contentDescription = "Ir al menú",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTicket,
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Ticket")
            }
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Buscar ticket") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Buscar")
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTickets) { ticket ->
                    TicketRow(
                        ticket = ticket,
                        tecnicoList = tecnicoList,
                        onEditTicket = onEditTicket,
                        onDeleteTicket = onDeleteTicket,
                        onMessageTicket = onMessageTicket // Pasamos la función aquí
                    )
                }
            }
        }
    }
}

@Composable
fun TicketRow(
    ticket: TicketEntity,
    tecnicoList: List<TecnicoEntity>,
    onEditTicket: (TicketEntity) -> Unit,
    onDeleteTicket: (TicketEntity) -> Unit,
    onMessageTicket: (TicketEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val tecnico = tecnicoList.find { it.tecnicoId == ticket.tecnicoId }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(5f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Asunto: ${ticket.asunto}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Cliente: ${ticket.cliente}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Fecha: ${ticket.fecha}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Descripción: ${ticket.descripcion}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Prioridad: ${ticket.prioridadId}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
                Text(
                    text = "Técnico: ${tecnico?.nombres ?: "Desconocido"}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                )
            }

            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                offset = DpOffset(x = (220).dp, y = 0.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("Mensaje") },
                    leadingIcon = { Icon(Icons.Filled.MailOutline, contentDescription = "Mensaje") },
                    onClick = {
                        onMessageTicket(ticket)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Editar") },
                    leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = "Editar") },
                    onClick = {
                        onEditTicket(ticket)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar") },
                    leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") },
                    onClick = {
                        onDeleteTicket(ticket)
                        expanded = false
                    }
                )
            }
        }
    }
}
