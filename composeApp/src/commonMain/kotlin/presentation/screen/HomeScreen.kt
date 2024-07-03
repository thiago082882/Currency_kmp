package presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import data.remore.api.CurrencyApiServiceImpl


class HomeScreen : Screen {
    @Composable
    override fun Content() {
        LaunchedEffect(Unit){
            CurrencyApiServiceImpl().getLatestExchangeRates()
        }
    }
}