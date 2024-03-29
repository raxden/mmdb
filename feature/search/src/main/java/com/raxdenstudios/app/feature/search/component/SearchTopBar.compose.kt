package com.raxdenstudios.app.feature.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Typography
import com.raxdenstudios.app.feature.search.model.SearchBarModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter

private const val SEARCH_DELAY = 500L

@OptIn(ExperimentalComposeUiApi::class, FlowPreview::class)
@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(4.dp),
    model: SearchBarModel,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusManager: FocusManager = LocalFocusManager.current,
    onSearchClick: (String) -> Unit = {},
    onSearchTextChanged: (String) -> Unit = {},
    onSearchClearClick: () -> Unit = {},
) {
    var searchText by remember { mutableStateOf("") }
    val searchTextFlow = remember { MutableStateFlow("") }

    LaunchedEffect(searchTextFlow) {
        searchTextFlow
            .filter { text -> text.isNotBlank() }
            .debounce(SEARCH_DELAY)
            .collect { text -> onSearchTextChanged(text) }
    }

    TopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = contentPadding,
        backgroundColor = MaterialTheme.colors.background,
    ) {
        TextField(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(100.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            textStyle = Typography.body1,
            value = when (model) {
                SearchBarModel.Focused -> searchText
                SearchBarModel.Idle -> searchText
                is SearchBarModel.Searching -> model.query
                is SearchBarModel.WithResults -> model.query
                is SearchBarModel.WithoutResults -> model.query
            },
            onValueChange = { value ->
                searchText = value
                searchTextFlow.value = value
            },
            placeholder = { Placeholder() },
            leadingIcon = { LeadingIcon() },
            trailingIcon = {
                TrailingIcon(
                    model = model,
                    onClearClick = {
                        searchText = ""
                        onSearchClearClick()
                    }
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchText.isBlank()) return@KeyboardActions
                    onSearchClick(searchText)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
        )
    }
}

@Composable
private fun LeadingIcon() {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Icon(
            painter = painterResource(id = AppIcons.Search),
            tint = MaterialTheme.colors.primary,
            contentDescription = null,
        )
    }
}

@Composable
private fun TrailingIcon(
    model: SearchBarModel,
    onClearClick: () -> Unit = {},
) {
    when (model) {
        SearchBarModel.Focused -> Unit
        SearchBarModel.Idle -> Unit
        is SearchBarModel.Searching -> SearchingIcon()
        is SearchBarModel.WithResults -> ClearButtonIcon(onClearClick)
        is SearchBarModel.WithoutResults -> ClearButtonIcon(onClearClick)
    }
}

@Composable
private fun SearchingIcon() {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
private fun ClearButtonIcon(
    onClearClick: () -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        IconButton(
            onClick = {
                onClearClick()
            }
        ) {
            Icon(
                imageVector = AppIcons.Clear,
                tint = MaterialTheme.colors.primary,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun Placeholder(
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        style = Typography.body2,
        text = stringResource(id = R.string.search_bar_hint)
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@DevicePreviews
@Composable
fun LoadingSearchTopBarPreview() {
    AppComposeTheme {
        SearchTopBar(
            model = SearchBarModel.Searching(
                query = "My Search Text"
            )
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@DevicePreviews
@Composable
fun DefaultSearchTopBarPreview() {
    AppComposeTheme {
        SearchTopBar(
            model = SearchBarModel.Idle
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@DevicePreviews
@Composable
fun SearchTopBarPreview() {
    AppComposeTheme {
        SearchTopBar(
            model = SearchBarModel.WithResults(
                query = "My Search Text",
            )
        )
    }
}
