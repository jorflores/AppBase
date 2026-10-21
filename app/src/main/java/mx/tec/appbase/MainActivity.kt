package mx.tec.appbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.tec.appbase.navigation.AppNavigation
import mx.tec.appbase.ui.theme.AppBaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Si tu proyecto tiene otro nombre, el tema también se llama distinto
            // (por ejemplo MiProyectoTheme). Revisa ui/theme/Theme.kt.
            AppBaseTheme {
                AppNavigation()
            }
        }
    }
}
