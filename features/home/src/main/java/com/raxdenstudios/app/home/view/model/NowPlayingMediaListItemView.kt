package com.raxdenstudios.app.home.view.model

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.NowPlayingMediaListItemViewBinding
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.loadImage
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.viewBinding

internal class NowPlayingMediaListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    val binding: NowPlayingMediaListItemViewBinding by viewBinding()

    var onMediaClickListener: (MediaListItemModel) -> Unit = {}
    var onMediaPlayClickListener: (MediaListItemModel) -> Unit = {}
    var onWatchListClickListener: (MediaListItemModel) -> Unit = {}

    init {
        if (isInEditMode) {
            inflateView(R.layout.now_playing_media_list_item_view, true)
        }
    }

    fun setModel(model: MediaListItemModel) {
        binding.populate(model)
    }

    private fun NowPlayingMediaListItemViewBinding.populate(model: MediaListItemModel) {
        mediaBackdropImageView.loadImage(model.backdrop)
        mediaBackdropImageView.setSafeOnClickListener { onMediaClickListener(model) }
        mediaBackdropPlayImageView.setSafeOnClickListener { onMediaPlayClickListener(model) }
        mediaImageCardView.setSafeOnClickListener { onMediaClickListener(model) }
        mediaImageView.loadImage(model.image)
        watchButtonView.setModel(model.watchButton)
        watchButtonView.setSafeOnClickListener { onWatchListClickListener(model) }
        movieTitleView.text = model.title
        movieDescriptionView.text = "The Rise of Sacha Baron Cohen"
    }
}
