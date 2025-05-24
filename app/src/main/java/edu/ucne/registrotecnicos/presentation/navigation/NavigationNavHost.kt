package edu.ucne.registrotecnicos.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.presentation.HomeScreen
import edu.ucne.registrotecnicos.presentation.mensaje.MensajeScreen
import edu.ucne.registrotecnicos.presentation.tecnico.DeleteTecnicoScreen
import edu.ucne.registrotecnicos.presentation.tecnico.EditTecnicoScreen
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoScreen
import edu.ucne.registrotecnicos.presentation.ticket.DeleteTicketScreen
import edu.ucne.registrotecnicos.presentation.ticket.EditTicketScreen
import edu.ucne.registrotecnicos.presentation.ticket.TicketListScreen
import edu.ucne.registrotecnicos.presentation.ticket.TicketScreen


@Composable
fun registro_tecnicos_tickets(tecnicoDb: TecnicoDb, navHostController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val tecnicoList by tecnicoDb.tecnicoDao().getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )
    val ticketList by tecnicoDb.ticketDao().getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )

    val scope = rememberCoroutineScope()
    NavHost(
        navController = navHostController,
        startDestination = Screen.HomeScreen
    ) {
        composable<Screen.HomeScreen> {
            HomeScreen(
                goToTecnico = {
                    navHostController.navigate(Screen.TecnicoList)
                },
                goToTickets = {
                    navHostController.navigate(Screen.TicketList)
                }
            )
        }


        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                drawerState = drawerState,
                scope = scope,
                createTecnico = {
                    navHostController.navigate(Screen.Tecnico(0))
                },
                onEditTecnico = { tecnicoId ->
                    navHostController.navigate(Screen.EditTecnico(tecnicoId))
                },
                onDeleteTecnico = { tecnicoId ->
                    navHostController.navigate(Screen.DeleteTecnico(tecnicoId))
                }
            )
        }

        composable<Screen.Tecnico> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Tecnico>()
            TecnicoScreen(
                goBack = { navHostController.popBackStack() }
            )
        }

        composable<Screen.EditTecnico> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditTecnico>()
            EditTecnicoScreen(
                tecnicoId = args.tecnicoId,
                goBack = { navHostController.popBackStack() }
            )
        }

        composable<Screen.DeleteTecnico> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.DeleteTecnico>()
            DeleteTecnicoScreen(
                tecnicoId = args.tecnicoId,
                goBack = { navHostController.popBackStack() }
            )
        }


        composable<Screen.TicketList> {
            TicketListScreen(
                drawerState = drawerState,
                scope = scope,
                createTicket = {
                    navHostController.navigate(Screen.Ticket(0))
                },
                onEditTicket = { ticket ->
                    navHostController.navigate(Screen.EditTicket(ticket.ticketId ?: 0))
                },
                onDeleteTicket = { ticket ->
                    navHostController.navigate(Screen.DeleteTicket(ticket.ticketId ?: 0))
                }
            )
        }

        composable<Screen.Ticket> {
            val args = it.toRoute<Screen.Ticket>()
            TicketScreen(
                goBack = {
                    navHostController.navigateUp()
                },
                goToMensajeScreen = { navHostController.navigate("MensajeScreen") }
            )
        }
        composable("MensajeScreen") {
            MensajeScreen()
        }

        composable<Screen.EditTicket> {
            val args = it.toRoute<Screen.EditTicket>()
            EditTicketScreen(
                ticketId = args.ticketId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }

        composable<Screen.DeleteTicket> {
            val args = it.toRoute<Screen.DeleteTicket>()
            DeleteTicketScreen(
                ticketId = args.ticketId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }


    }
}