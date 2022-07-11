package com.raxdenstudios.app.navigator.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.list.view.MediaListActivity
import com.raxdenstudios.app.navigator.mapper.MediaListParamsMapper
import javax.inject.Inject

internal class MediaListActivityResultContract @Inject constructor(
  private val mediaListParamsMapper: MediaListParamsMapper
) : ActivityResultContract<HomeModuleModel.CarouselMedias, Boolean>() {

  override fun createIntent(
    context: Context,
    carouselMedias: HomeModuleModel.CarouselMedias
  ): Intent {
    val params = mediaListParamsMapper.transform(carouselMedias)
    return MediaListActivity.createIntent(context, params)
  }

  override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
    resultCode == Activity.RESULT_OK
}
