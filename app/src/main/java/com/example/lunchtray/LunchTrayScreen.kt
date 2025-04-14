package com.example.lunchtray
//lu
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.*

enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Entree(title = R.string.choose_entree),
    SideDish(title = R.string.choose_side_dish),
    Accompaniment(title = R.string.choose_accompaniment),
    Checkout(title = R.string.order_checkout)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    @StringRes currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = currentScreenTitle)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        },
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun LunchTrayApp() {

        val navController = rememberNavController()

        val backStackEntry by navController.currentBackStackEntryAsState()

        val currentScreen = LunchTrayScreen.valueOf(
            backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name
        )

    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            LunchTrayAppBar(
                currentScreenTitle = currentScreen.title,
                canNavigateBack = currentScreen != LunchTrayScreen.Start,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(LunchTrayScreen.Start.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = { navController.navigate(LunchTrayScreen.Entree.name) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(LunchTrayScreen.Entree.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.SideDish.name) },
                    onSelectionChanged = { viewModel.updateEntree(it) },
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
            composable(LunchTrayScreen.SideDish.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Accompaniment.name) },
                    onSelectionChanged = { viewModel.updateSideDish(it) },
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
            composable(LunchTrayScreen.Accompaniment.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = { navController.navigate(LunchTrayScreen.Checkout.name) },
                    onSelectionChanged = { viewModel.updateAccompaniment(it) },
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
            composable(LunchTrayScreen.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
                    },
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
        }
    }
}
