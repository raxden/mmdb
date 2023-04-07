package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.Header
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Grey200
import com.raxdenstudios.app.core.ui.theme.Grey700
import com.raxdenstudios.app.core.ui.theme.Typography

@Composable
fun MediaDetails(
    modifier: Modifier = Modifier,
    media: MediaModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Header(
            modifier = Modifier,
            title = stringResource(id = R.string.details),
        )
        CellData(
            modifier = Modifier,
            title = stringResource(id = R.string.original_language),
            description = media.originalLanguage,
        )
        if (media.budget.isNotEmpty()) {
            CellData(
                modifier = Modifier,
                title = stringResource(id = R.string.budget),
                description = media.budget,
            )
        }
        if (media.revenue.isNotEmpty()) {
            CellData(
                modifier = Modifier,
                title = stringResource(id = R.string.revenue),
                description = media.revenue,
            )
        }
        CellData(
            modifier = Modifier,
            title = stringResource(id = R.string.release_date),
            description = media.releaseDate,
        )
        CellData(
            modifier = Modifier,
            title = stringResource(id = R.string.language_spoken),
            description = media.spokenLanguages,
        )
    }
}

@Composable
fun CellData(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier,
            text = title,
            style = Typography.body1,
        )
        Text(
            modifier = Modifier,
            text = description,
            style = Typography.body2.copy(color = Grey700),
        )
        Divider(
            modifier = Modifier
                .padding(top = 8.dp),
            thickness = 1.dp,
            color = Grey200,
        )
    }
}

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaDetailsPreview() {
    AppComposeTheme {
        MediaDetails(
            media = MediaModel.mock,
        )
    }
}
