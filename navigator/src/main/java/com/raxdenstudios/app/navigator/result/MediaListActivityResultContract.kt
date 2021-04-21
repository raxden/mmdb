package com.raxdenstudios.app.navigator.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.raxdenstudios.app.list.view.MediaListActivity
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.movie.view.model.MediaFilterModel

internal class MediaListActivityResultContract :
  ActivityResultContract<MediaFilterModel, Boolean>() {

  override fun createIntent(context: Context, mediaFilterModel: MediaFilterModel): Intent {
    val params = MediaListParams(mediaFilterModel)
    return MediaListActivity.createIntent(context, params)
  }

  override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
    resultCode == Activity.RESULT_OK
}
