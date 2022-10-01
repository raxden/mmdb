package com.raxdenstudios.app.feature.home.model

import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel

sealed interface HomeModuleModel {

    val id: Long

    sealed class Carousel : HomeModuleModel {

        abstract val label: String
        abstract val medias: List<MediaModel>
        abstract val filters: List<MediaFilterModel>

        fun hasMedias(): Boolean = medias.isNotEmpty()

        data class Popular(
            override val id: Long,
            override val label: String,
            override val medias: List<MediaModel>,
            override val filters: List<MediaFilterModel>,
        ) : Carousel() {

            companion object {

                val empty = Popular(
                    id = 0L,
                    label = "",
                    medias = emptyList(),
                    filters = emptyList(),
                )
            }
        }

        data class Watchlist(
            override val id: Long,
            override val label: String,
            override val medias: List<MediaModel>,
            override val filters: List<MediaFilterModel>,
        ) : Carousel() {

            companion object {

                val empty = Watchlist(
                    id = 0L,
                    label = "",
                    medias = emptyList(),
                    filters = emptyList(),
                )
            }
        }

        data class NowPlaying(
            override val id: Long,
            override val label: String,
            override val medias: List<MediaModel>,
            override val filters: List<MediaFilterModel>,
        ) : Carousel() {

            companion object {

                val empty = NowPlaying(
                    id = 0L,
                    label = "",
                    medias = emptyList(),
                    filters = emptyList(),
                )
            }
        }

        data class TopRated(
            override val id: Long,
            override val label: String,
            override val medias: List<MediaModel>,
            override val filters: List<MediaFilterModel>,
        ) : Carousel() {

            companion object {

                val empty = TopRated(
                    id = 0L,
                    label = "",
                    medias = emptyList(),
                    filters = emptyList(),
                )
            }
        }

        data class Upcoming(
            override val id: Long,
            override val label: String,
            override val medias: List<MediaModel>,
            override val filters: List<MediaFilterModel>,
        ) : Carousel() {

            companion object {

                val empty = Upcoming(
                    id = 0L,
                    label = "",
                    medias = emptyList(),
                    filters = emptyList(),
                )
            }
        }
    }
}
