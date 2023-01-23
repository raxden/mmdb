package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.BlazeOrangeTranslucent
import com.raxdenstudios.app.core.ui.theme.Typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaTypeFilterChip(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean = false,
    onclick: () -> Unit = {},
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        border = BorderStroke(1.dp, MaterialTheme.colors.secondary).takeIf { isSelected },
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = BlazeOrangeTranslucent,
        ),
        onClick = onclick,
    ) {
        Text(
            style = Typography.caption,
            text = label,
        )
    }
}

@Preview
@Composable
fun MediaTypeFilterChipPreview() {
    AppComposeTheme {
        Column {
            MediaTypeFilterChip(label = "Movies")
            MediaTypeFilterChip(label = "Movies", isSelected = true)
        }
    }
}
