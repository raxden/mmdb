package com.raxdenstudios.app.feature.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.IdlingRegistry
import com.raxdenstudios.app.core.network.di.APIVersionV3
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.test.HiltTestActivity
import com.raxdenstudios.app.test.MockWebServerRule
import com.raxdenstudios.app.test.OkHttp3IdlingResource
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.utils.RequestMatchers
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
internal class HomeScreenInstrumentalTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    @APIVersionV3
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setup() {
        hiltRule.inject()

        val resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)

        RESTMockServer.whenGET(RequestMatchers.pathContains("movie/now_playing"))
            .thenReturnFile(200, "now_playing.json")
        RESTMockServer.whenGET(RequestMatchers.pathContains("movie/popular"))
            .thenReturnFile(200, "popular.json")
        RESTMockServer.whenGET(RequestMatchers.pathContains("movie/top_rated"))
            .thenReturnFile(200, "top_rated.json")
        RESTMockServer.whenGET(RequestMatchers.pathContains("movie/upcoming"))
            .thenReturnFile(200, "upcoming.json")
    }

    @Test
    fun should_display_HomeScreen() {
        composeTestRule.setContent {
            AppComposeTheme {
                HomeScreen()
            }
        }
    }
}
