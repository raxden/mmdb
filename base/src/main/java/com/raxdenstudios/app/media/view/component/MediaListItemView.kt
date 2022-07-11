package com.raxdenstudios.app.media.view.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.base.R
import com.raxdenstudios.app.base.databinding.MediaListItemViewBinding
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.loadImage
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.viewBinding

class MediaListItemView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding: MediaListItemViewBinding by viewBinding()

  var onAddToWatchListClickListener: (MediaListItemModel) -> Unit = {}
  var onRemoveFromWatchListClickListener: (MediaListItemModel) -> Unit = {}

  init {
    if (isInEditMode) {
      inflateView(R.layout.media_list_item_view, true)
    }
  }

  fun setModel(model: MediaListItemModel) {
    binding.populate(model)
  }

  private fun MediaListItemViewBinding.populate(model: MediaListItemModel) {
    mediaImageView.loadImage(model.image)
    movieTitleView.text = model.title
    movieRateView.text = model.rating
    movieSubtitleView.text = model.releaseDate
    watchButtonView.setModel(model.watchButtonModel)
    watchButtonView.setSafeOnClickListener {
      when (model.watchButtonModel) {
        WatchButtonModel.Selected -> onRemoveFromWatchListClickListener(model)
        WatchButtonModel.Unselected -> onAddToWatchListClickListener(model)
      }
    }
  }
}
