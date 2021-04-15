package com.raxdenstudios.app.movie.view.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.feature.base.R
import com.raxdenstudios.app.feature.base.databinding.MovieListItemViewBinding
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
import com.raxdenstudios.app.movie.view.model.WatchButtonModel
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.loadImage
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.viewBinding

class MovieListItemView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding: MovieListItemViewBinding by viewBinding()

  var onAddToWatchListClickListener: (MovieListItemModel) -> Unit = {}
  var onRemoveFromWatchListClickListener: (MovieListItemModel) -> Unit = {}

  init {
    if (isInEditMode) {
      inflateView(R.layout.movie_list_item_view, true)
    }
  }

  fun setModel(model: MovieListItemModel) {
    binding.populate(model)
  }

  private fun MovieListItemViewBinding.populate(model: MovieListItemModel) {
    movieImageView.loadImage(model.image)
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