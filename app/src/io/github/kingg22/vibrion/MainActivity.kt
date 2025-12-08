package io.github.kingg22.vibrion

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.platform.LocalContext
import io.github.kingg22.vibrion.navigation.Vibrion
import io.github.kingg22.vibrion.ui.screens.permissions.PermissionsHandlerScreen
import io.github.kingg22.vibrion.ui.theme.VibrionAppTheme
import io.github.kingg22.vibrion.ui.theme.isDarkTheme
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = this.applicationContext as MainApplication
        app.getMediaController(this)

        setContent {
            // TODO find a solution to init before draw this composable to avoid splash
            // Can use Splash Screen API to load before draw this composable
            val isDarkTheme = isDarkTheme(koinViewModel(viewModelStoreOwner = this))
            enableEdgeToEdge { isDarkTheme }

            val dynamicColorScheme: ColorScheme?
            // Dynamic color is available on Android 12+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val context = LocalContext.current
                dynamicColorScheme = if (isDarkTheme) {
                    dynamicDarkColorScheme(context)
                } else {
                    dynamicLightColorScheme(context)
                }
            } else {
                dynamicColorScheme = null
            }

            VibrionAppTheme(darkTheme = isDarkTheme, dynamicColorScheme = dynamicColorScheme) {
                Surface {
                    Vibrion()
                    PermissionsHandlerScreen()
                }
            }
        }
    }
}
