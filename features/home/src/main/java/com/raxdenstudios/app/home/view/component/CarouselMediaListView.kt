package com.raxdenstudios.app.home.view.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.CarouselMediaListViewBinding
import com.raxdenstudios.app.home.view.adapter.CarouselMediaListAdapter
import com.raxdenstudios.app.home.view.model.CarouselMediaListModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.viewBinding

internal class CarouselMediaListView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding: CarouselMediaListViewBinding by viewBinding()

  private val adapter: CarouselMediaListAdapter by lazy { CarouselMediaListAdapter() }

  var onAddToWatchListClickListener: (CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onRemoveFromWatchListClickListener: (CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onMediaClickListener: (CarouselMediaListModel, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onSeeAllClickListener: (CarouselMediaListModel) -> Unit = {}

  init {
    if (isInEditMode) {
      inflateView(R.layout.carousel_media_list_view, true)
    } else {
      binding.recyclerView.adapter = adapter
    }
  }

  fun setModel(model: CarouselMediaListModel) {
    binding.populate(model)
  }

  private fun CarouselMediaListViewBinding.populate(model: CarouselMediaListModel) {
    carouselTitleView.text = model.label
    adapter.submitList(model.medias)
    adapter.onClickListener = { item -> onMediaClickListener(model, item) }
    adapter.onAddToWatchListClickListener =
      { item -> onAddToWatchListClickListener(model, item) }
    adapter.onRemoveFromWatchListClickListener =
      { item -> onRemoveFromWatchListClickListener(model, item) }
    carouselSeeAllArrowViewGroup.setSafeOnClickListener { onSeeAllClickListener(model) }
  }
}
