import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.Header
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun RecentSearches(
    modifier: Modifier = Modifier,
    searches: List<String>,
    onRecentSearchClicked: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Header(title = stringResource(id = R.string.recent_searches))
        Divider()
        LazyColumn(
            modifier = modifier
                .fillMaxWidth(),
            content = {
                items(searches.size) { index ->
                    val recentSearch = searches[index]
                    RecentSearch(
                        modifier = Modifier
                            .clickable { onRecentSearchClicked(recentSearch) },
                        label = recentSearch
                    )
                    Divider()
                }
            }
        )
    }
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
