package com.raxdenstudios.app.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.raxdenstudios.app.ui.model.BottomBarItemModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Typography

@Composable
fun MainBottomBar(
    modifier: Modifier = Modifier,
    items: List<BottomBarItemModel>,
    onItemClick: (BottomBarItemModel) -> Unit = {},
) {
    BottomNavigation(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        items.forEach { item ->
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

@Preview(showBackground = true)
@Composable
fun MainBottomBarPreview() {
    AppComposeTheme {
        MainBottomBar(
            items = BottomBarItemModel.default,
        )
    }
}
