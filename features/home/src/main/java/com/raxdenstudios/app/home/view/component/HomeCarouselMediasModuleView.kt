package com.raxdenstudios.app.home.view.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeCarouselMediasModuleViewBinding
import com.raxdenstudios.app.home.view.adapter.CarouselMediaListAdapter
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.*

internal class HomeCarouselMediasModuleView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding: HomeCarouselMediasModuleViewBinding by viewBinding()

  private val adapter: CarouselMediaListAdapter by lazy { CarouselMediaListAdapter() }

  var onAddToWatchListClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onRemoveFromWatchListClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onMediaClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onSeeAllClickListener: (HomeModuleModel.CarouselMedias) -> Unit = {}
  var onMediaTypeFilterChanged: (HomeModuleModel.CarouselMedias, MediaType) -> Unit = { _, _ -> }
  var onSigInClickListener: (HomeModuleModel.CarouselMedias) -> Unit = {}

  init {
    if (isInEditMode) {
      inflateView(R.layout.home_carousel_medias_module_view, true)
    } else {
      binding.recyclerView.adapter = adapter
    }
  }

  fun setModel(model: HomeModuleModel.CarouselMedias) {
    binding.populate(model)
  }

  private fun HomeCarouselMediasModuleViewBinding.populate(model: HomeModuleModel.CarouselMedias) {
    setTitle(model)
    populateRecyclerView(model)
    seeAllButton(model)
    filterSelector(model)
  }

  private fun HomeCarouselMediasModuleViewBinding.setTitle(model: HomeModuleModel.CarouselMedias) {
    carouselTitleView.text = model.label
  }

  private fun HomeCarouselMediasModuleViewBinding.populateRecyclerView(model: HomeModuleModel.CarouselMedias) {
    adapter.submitList(model.medias)
    adapter.onClickListener = { item -> onMediaClickListener(model, item) }
    adapter.onAddToWatchListClickListener = { item -> onAddToWatchListClickListener(model, item) }
    adapter.onRemoveFromWatchListClickListener = { item ->
      onRemoveFromWatchListClickListener(model, item)
    }

    val hasMedias = model.hasMedias()
    val modelIsWatchList = model is HomeModuleModel.CarouselMedias.WatchList
    val modelIsWatchListAndUserIsNotLogged =
      model is HomeModuleModel.CarouselMedias.WatchList && model.requireSigIn
    when {
      hasMedias -> {
        recyclerView.doItVisible()
        emptyWatchListView.root.doItGone()
        siginWatchListView.root.doItGone()
      }
      modelIsWatchListAndUserIsNotLogged -> {
        recyclerView.doItGone()
        emptyWatchListView.root.doItGone()
        siginWatchListView.root.doItVisible()
        siginWatchListView.sigIn.setSafeOnClickListener { onSigInClickListener(model) }
      }
      modelIsWatchList -> {
        recyclerView.doItGone()
        emptyWatchListView.root.doItVisible()
        siginWatchListView.root.doItGone()
      }
    }
  }

  private fun HomeCarouselMediasModuleViewBinding.seeAllButton(model: HomeModuleModel.CarouselMedias) {
    carouselSeeAllArrowViewGroup.visibleGone(model.hasMedias())
    carouselSeeAllArrowViewGroup.setSafeOnClickListener { onSeeAllClickListener(model) }
  }

  private fun HomeCarouselMediasModuleViewBinding.filterSelector(model: HomeModuleModel.CarouselMedias) {
    mediaTypeChipGroup.visibleGone(model.hasMediaTypeFilter)
    moviesChip.isChecked = model.mediaType == MediaType.MOVIE
    tvSeriesChip.isChecked = model.mediaType == MediaType.TV_SHOW
    mediaTypeChipGroup.setOnCheckedChangeListener { _, checkedId ->
      val mediaType = when (checkedId) {
        R.id.movies_chip -> MediaType.MOVIE
        R.id.tv_series_chip -> MediaType.TV_SHOW
        else -> throw IllegalStateException("Invalid mediaType option selected")
      }
      onMediaTypeFilterChanged(model, mediaType)
    }
  }
}
