
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.app.core.ui.component.Body2Text
import com.raxdenstudios.app.core.ui.component.RightArrowIcon

@Composable
fun SeeAllButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(start = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Body2Text(text = stringResource(id = R.string.home_carousel_see_all).uppercase())
        RightArrowIcon()
    }
}
