package com.raxdenstudios.app.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.core.navigation.NavigationRoute
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Typography
import com.raxdenstudios.app.ui.BottomBarContract
import com.raxdenstudios.app.ui.BottomBarViewModel
import com.raxdenstudios.app.ui.model.BottomBarItemModel

@Composable
fun MainBottomBar(
    modifier: Modifier = Modifier,
    viewModel: BottomBarViewModel = hiltViewModel(),
    onNavigateTo: (NavigationRoute) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    uiState.events.firstOrNull()?.let { event ->
        when (event) {
            BottomBarContract.UIEvent.NavigateToAccount -> onNavigateTo(MainRoutes.account)
            BottomBarContract.UIEvent.NavigateToHome -> onNavigateTo(MainRoutes.home)
            BottomBarContract.UIEvent.NavigateToSearch -> onNavigateTo(MainRoutes.search)
        }
        viewModel.eventConsumed(event)
    }

    MainBottomBar(
        modifier = modifier,
        uiState = uiState,
        onItemClick = { item ->
            val userEvent = BottomBarContract.UserEvent.ItemSelected(item)
            viewModel.setUserEvent(userEvent)
        },
    )
}


@Composable
fun MainBottomBar(
    modifier: Modifier = Modifier,
    uiState: BottomBarContract.UIState,
    onItemClick: (BottomBarItemModel) -> Unit = {},
) {
    BottomNavigation(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        uiState.items.forEach { item ->
            BottomNavigationItem(
                modifier = modifier,
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.label),
                    )
                },
                label = {
                    Text(
                        style = Typography.caption,
                        text = stringResource(id = item.label),
                    )
                },
                selected = item.isSelected,
                onClick = { onItemClick(item) },
            )
        }
    }
}

@DevicePreviews
@Composable
fun MainBottomBarPreview() {
    AppComposeTheme {
        MainBottomBar(
            uiState = BottomBarContract.UIState(),
        )
    }
}
