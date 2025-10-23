package com.example.birthday

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.birthday.data.AppDatabase
import com.example.birthday.repositories.PersonRepository
import com.example.birthday.service.scheduleSmsWorker
import com.example.birthday.ui.screens.AddScreen
import com.example.birthday.ui.screens.ListScreen
import com.example.birthday.ui.screens.PreferencesScreen
import com.example.birthday.ui.screens.StartScreen
import com.example.birthday.ui.theme.BirthdayTheme
import com.example.birthday.ui.viewmodels.PersonViewModel
import com.example.birthday.ui.viewmodels.SmsViewModel


class MainActivity : ComponentActivity() {
    private val beOmSmstillatelse = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean ->
        if (isGranted) {
            Log.d("SMS", "Tillatelse til å sende SMS")
        } else {
            Log.d("SMS", "Ikke tillatelse til å sende SMS")

        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
         Log.d("SMS", "Tillatelse til å sende SMS")
        } else {
            beOmSmstillatelse.launch(Manifest.permission.SEND_SMS)
        }

        scheduleSmsWorker(this)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "person_db"
        ).build()

        val repository = PersonRepository(db.personDao())
        val viewModel = PersonViewModel(repository, application)
        val smsViewModel : SmsViewModel = SmsViewModel(applicationContext as Application)



        enableEdgeToEdge()
        setContent {
            BirthdayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Birthday(modifier = Modifier.padding(innerPadding), viewModel, smsViewModel)
                }
            }
        }
    }
}

@Composable
fun Birthday(modifier: Modifier = Modifier, viewModel: PersonViewModel, smsViewModel: SmsViewModel) {
    val navController = rememberNavController()
    NavigationGraph(navController = navController, viewModel = viewModel, smsViewModel = smsViewModel)
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: PersonViewModel, smsViewModel: SmsViewModel) {
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController = navController) }
        composable("list") { ListScreen(navController = navController, viewModel, smsViewModel = smsViewModel) }
        composable("add") { AddScreen(navController = navController) }
        composable("preferences") { PreferencesScreen(navController = navController) }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BirthdayTheme {
        Greeting("Android")
    }
}


