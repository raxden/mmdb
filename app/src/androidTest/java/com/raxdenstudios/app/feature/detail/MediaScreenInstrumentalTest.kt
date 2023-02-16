package com.raxdenstudios.app.feature.detail

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import com.raxdenstudios.app.HiltActivity
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.network.di.APIVersionV3
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.detail.model.MediaParams
import com.raxdenstudios.app.test.MockWebServerRule
import com.raxdenstudios.app.test.OkHttp3IdlingResource
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.utils.RequestMatchers.pathContains
import io.mockk.every
import io.mockk.mockk
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
internal class MediaScreenInstrumentalTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltActivity>()

    @BindValue
    val mediaParamsFactory: MediaParamsFactory = mockk {
        every { create() } returns params
    }

    @Inject
    @APIVersionV3
    lateinit var okHttpClient: OkHttpClient

    private lateinit var resource: IdlingResource

    @Before
    fun setup() {
        hiltRule.inject()

        resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(resource)
    }

    @Test
    fun should_display_MediaScreen() {
        RESTMockServer.whenGET(pathContains("/movie/1"))
            .thenReturnFile(200, "media.json")

        composeTestRule.run {
            setContent {
                AppComposeTheme {
                    MediaScreen()
                }
            }
            onNodeWithContentDescription("Media Header").assertExists()
            onNodeWithContentDescription("Media Body").assertExists()
            onNodeWithContentDescription("Back Button").assertExists()
            onNodeWithContentDescription("Add to watchlist").assertExists()
        }
    }

    @Test
    fun should_display_an_error_dialog() {
        RESTMockServer.whenGET(pathContains("/movie/1"))
            .thenReturnString(400, "")

        composeTestRule.run {
            setContent {
                AppComposeTheme {
                    MediaScreen()
                }
            }
            onNodeWithContentDescription("Error Dialog").assertExists()
        }
    }

    companion object {

        val params = MediaParams(
            mediaId = MediaId(1),
            mediaType = MediaType.Movie
        )
    }
}
