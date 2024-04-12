package com.example.yamicomputer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.yamicomputer.logic.SharedViewModel
import com.example.yamicomputer.navigation.NavGraph
import com.example.yamicomputer.ui.theme.YamiComputerTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val viewModel: SharedViewModel by viewModels()

    private lateinit var chooseImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                
            }
        }

        setContent {
            YamiComputerTheme {

            navController = rememberNavController()
            NavGraph(navHostController = navController, sharedViewModel = viewModel, componentActivity = this@MainActivity)

            }
        }
    }
}

