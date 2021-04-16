package com.raxdenstudios.app.movie.view.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.feature.base.R
import com.raxdenstudios.app.feature.base.databinding.WatchButtonViewBinding
import com.raxdenstudios.app.movie.view.model.WatchButtonModel
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.viewBinding

internal class WatchButtonView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val binding: WatchButtonViewBinding by viewBinding()

  init {
    if (isInEditMode) {
      inflateView(R.layout.watch_button_view, true)
    }
  }

  fun setModel(model: WatchButtonModel) {
    binding.populate(model)
  }

  private fun WatchButtonViewBinding.populate(model: WatchButtonModel) {
    root.isSelected = when (model) {
      WatchButtonModel.Selected -> true
      WatchButtonModel.Unselected -> false
    }
  }
}