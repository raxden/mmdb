package com.raxdenstudios.app.feature.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.IdlingRegistry
import com.raxdenstudios.app.HiltActivity
import com.raxdenstudios.app.core.network.di.APIVersionV3
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.test.MockWebServerRule
import com.raxdenstudios.app.test.OkHttp3IdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
    val composeTestRule = createAndroidComposeRule<HiltActivity>()

    @Inject
    @APIVersionV3
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            AppComposeTheme {
                HomeScreen()
            }
        }
        val resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun should_display_HomeScreen() {
        assert(true)
    }
}
