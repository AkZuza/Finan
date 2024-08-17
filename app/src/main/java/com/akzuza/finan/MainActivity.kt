package com.akzuza.finan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.room.Room
import com.akzuza.finan.data.FinanTransactionDatabase
import com.akzuza.finan.ui.home.FinanHomeViewModel
import com.akzuza.finan.ui.theme.FinanTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinanTransactionDatabase::class.java,
            "finan-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    private val homeViewModel: FinanHomeViewModel by viewModels(
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FinanHomeViewModel(transactionsDao = db.dao()) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanTheme {
                val finanHomeState by homeViewModel.uiState.collectAsState()

                FinanApp(finanHomeState, homeViewModel::onEvent)
            }
        }
    }
}
