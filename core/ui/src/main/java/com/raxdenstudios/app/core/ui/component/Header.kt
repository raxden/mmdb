package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.R
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.DeepOrange500
import com.raxdenstudios.app.core.ui.theme.Typography

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
    seeAllVisible: Boolean = false,
    onSeeAllClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            modifier = Modifier,
            painter = painterResource(id = AppIcons.DoubleArrow),
            tint = DeepOrange500,
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp),
            style = Typography.h6.copy(
                fontWeight = FontWeight.Bold,
            ),
            text = title,
        )
        if (seeAllVisible) {
            Row(
                modifier = Modifier
                    .clickable { onSeeAllClick() }
                    .padding(start = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier,
                    style = Typography.body2.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    text = stringResource(id = R.string.home_carousel_see_all).uppercase(),
                )
                Icon(
                    modifier = Modifier,
                    painter = painterResource(id = AppIcons.RightArrow),
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = null
                )
            }
        }
    }
}

@DevicePreviews
@Composable
fun HeaderPreview() {
    AppComposeTheme {
        Header(title = "Popular")
    }
}

@DevicePreviews
@Composable
fun HeaderSeeAllPreview() {
    AppComposeTheme {
        Header(title = "Popular", seeAllVisible = true)
    }
}
