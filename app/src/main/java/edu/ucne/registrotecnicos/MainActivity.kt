package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.presentation.navigation.registro_tecnicos_tickets
import edu.ucne.registrotecnicos.presentation.ticket.MainScreen
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroTecnicosTheme {
                val navController = rememberNavController()
                val tecnicoDb = Room.databaseBuilder(
                    applicationContext,
                    TecnicoDb::class.java,
                    "TecnicoDb"
                ).build()

                MainScreen(context = this)

                registro_tecnicos_tickets(
                    navHostController = navController,
                    tecnicoDb = tecnicoDb
                )
            }
        }
    }
}
