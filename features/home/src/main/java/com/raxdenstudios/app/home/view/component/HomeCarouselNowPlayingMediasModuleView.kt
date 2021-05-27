package com.raxdenstudios.app.home.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeCarouselNowPlayingMediasModuleViewBinding
import com.raxdenstudios.app.home.view.adapter.CarouselNowPlayingMediaListAdapter
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.home.view.model.NowPlayingMediaListItemView
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.viewBinding
import com.raxdenstudios.commons.ext.visibleGone

internal class HomeCarouselNowPlayingMediasModuleView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding: HomeCarouselNowPlayingMediasModuleViewBinding by viewBinding()

  private val viewPager: ViewPager2 by lazy { binding.viewPager }
  private val viewPagerTransformer: CarouselNowPlayingMediasPageTransformer by lazy {
    CarouselNowPlayingMediasPageTransformer()
  }
  private val adapter: CarouselNowPlayingMediaListAdapter by lazy {
    CarouselNowPlayingMediaListAdapter()
  }

  var onAddToWatchListClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onRemoveFromWatchListClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onMediaClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onMediaPlayClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
    { _, _ -> }
  var onSeeAllClickListener: (HomeModuleModel.CarouselMedias) -> Unit = {}

  init {
    if (isInEditMode) {
      inflateView(R.layout.home_carousel_now_playing_medias_module_view, true)
    } else {
      viewPager.adapter = adapter
      viewPager.setPageTransformer(viewPagerTransformer)
    }
  }

  fun setModel(model: HomeModuleModel.CarouselMedias.NowPlaying) {
    binding.populate(model)
  }

  private fun HomeCarouselNowPlayingMediasModuleViewBinding.populate(
    model: HomeModuleModel.CarouselMedias.NowPlaying
  ) {
    setTitle(model)
    adapter.populate(model)
    seeAllButton(model)
  }

  private fun HomeCarouselNowPlayingMediasModuleViewBinding.setTitle(
    model: HomeModuleModel.CarouselMedias
  ) {
    homeCarouselMediasTitleModuleView.carouselTitleView.text = model.label
  }

  private fun CarouselNowPlayingMediaListAdapter.populate(
    model: HomeModuleModel.CarouselMedias.NowPlaying
  ) {
    submitList(model.medias)
    onMediaClickListener = { item -> onMediaClickListener(model, item) }
    onMediaPlayClickListener = { item -> onMediaPlayClickListener(model, item) }
    onAddToWatchListClickListener = { item -> onAddToWatchListClickListener(model, item) }
    onRemoveFromWatchListClickListener = { item -> onRemoveFromWatchListClickListener(model, item) }
  }

  private fun HomeCarouselNowPlayingMediasModuleViewBinding.seeAllButton(
    model: HomeModuleModel.CarouselMedias
  ) {
    homeCarouselMediasTitleModuleView.carouselSeeAllArrowViewGroup.apply {
      visibleGone(model.hasMedias())
      setSafeOnClickListener { onSeeAllClickListener(model) }
    }
  }
}

internal class CarouselNowPlayingMediasPageTransformer : ViewPager2.PageTransformer {

  override fun transformPage(page: View, position: Float) {
    if (isPageUnderTransition(position)) animateViews(page, position)
  }

  private fun animateViews(page: View, position: Float) {
    val view = page as? NowPlayingMediaListItemView ?: return
    val translationX = view.width * position
    view.binding.movieTitleView.translationX = translationX
    view.binding.movieDescriptionView.translationX = translationX
    view.binding.mediaImageCardView.translationX = translationX
  }

  private fun isPageUnderTransition(position: Float) = position in -1.0..1.0
}
