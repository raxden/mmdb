
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun RecentSearch(
    modifier: Modifier = Modifier,
    label: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Icon(
            modifier = Modifier,
            imageVector = AppIcons.RecentSearch,
            tint = MaterialTheme.colors.primary,
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .weight(1f),
            text = label
        )
        Icon(
            modifier = Modifier,
            imageVector = AppIcons.RecentSearchSelect,
            tint = MaterialTheme.colors.primary,
            contentDescription = null,
        )
    }
}

@DevicePreviews
@Composable
fun RecentSearchPreview() {
    AppComposeTheme {
        RecentSearch(
            label = "My Recent Search Text",
        )
    }
}
