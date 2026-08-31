package fr.brendan.currconv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import fr.brendan.currconv.navigation.CurrConvNav
import fr.brendan.currconv.ui.theme.CurrConvTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrConvTheme {
                CurrConvNav()
            }
        }
    }
}