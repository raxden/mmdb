package com.raxdenstudios.app.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.feature.home.R

sealed class BottomBarItemModel(
    val id: Long,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {

    abstract val isSelected: Boolean

    fun copyValues(
        isSelected: Boolean = this.isSelected,
    ) = when (this) {
        is Home -> copy(isSelected = isSelected)
        is Account -> copy(isSelected = isSelected)
        is Search -> copy(isSelected = isSelected)
    }

    data class Home(
        override val isSelected: Boolean = false,
    ) : BottomBarItemModel(
        id = 1L,
        label = R.string.home_navigation_home,
        icon = AppIcons.Home,
    )

    data class Search(
        override val isSelected: Boolean = false,
    ) : BottomBarItemModel(
        id = 2L,
        label = R.string.home_navigation_search,
        icon = AppIcons.Search,
    )

    data class Account(
        override val isSelected: Boolean = false,
    ) : BottomBarItemModel(
        id = 3L,
        label = R.string.home_navigation_account,
        icon = AppIcons.Account,
    )

    companion object {

        val default = listOf(
            Home(isSelected = true),
            Search(),
            Account(),
        )
    }
}
