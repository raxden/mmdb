import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun RecentSearches(
    modifier: Modifier = Modifier,
    searches: List<String>,
    onRecentSearchClicked: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        content = {
            items(searches.size) { index ->
                val recentSearch = searches[index]
                val lastIndex: Boolean = index == searches.lastIndex
                RecentSearch(
                    modifier = Modifier
                        .clickable { onRecentSearchClicked(recentSearch) },
                    label = recentSearch
                )
                if (!lastIndex) Divider()
            }
        }
    )
}

@DevicePreviews
@Composable
fun RecentSearchesPreview() {
    AppComposeTheme {
        RecentSearches(
            searches = listOf("My Recent Search Text", "My Recent Search Text 2", "My Recent Search Text 3"),
        )
    }
}
