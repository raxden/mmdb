package com.raxdenstudios.app.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    model: ErrorModel,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier
            .semantics { contentDescription = "Error Dialog" },
        title = {
            Text(
                style = MaterialTheme.typography.body1,
                text = model.title,
            )
        },
        text = {
            Text(
                style = MaterialTheme.typography.body1,
                text = model.message,
            )
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                content = {
                    Text(
                        style = MaterialTheme.typography.caption,
                        text = "OK"
                    )
                },
                onClick = { onDismiss() },
            )
        }
    )
}


@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun ErrorDialogPreview() {
    AppComposeTheme {
        ErrorDialog(
            model = ErrorModel.mock,
        )
    }
}
