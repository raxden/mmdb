package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.commons.android.provider.StringProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.threeten.bp.Duration

class DurationModelMapperTest {

    private val stringProvider: StringProvider = mockk {
        every { getString(R.string.hour, 1L) } returns "1 hour"
        every { getString(R.string.minutes, 30) } returns "30 minutes"
        every { getString(R.string.hour_minutes, 1L, 30) } returns "1 hour 30 minutes"
        every { getString(R.string.hours, 2L) } returns "2 hours"
        every { getString(R.string.hours_minutes, 2L, 30) } returns "2 hours 30 minutes"
    }
    private lateinit var mapper: DurationModelMapper

    @Before
    fun setUp() {
        mapper = DurationModelMapper(
            stringProvider = stringProvider,
        )
    }

    @Test
    fun `given an empty duration when transform then return an empty string`() {
        // Given
        val duration = Duration.ZERO

        // When
        val result = mapper.transform(duration)

        // Then
        assert(result == "")
    }

    @Test
    fun `given a duration of 30 minutes when transform then return a duration string`() {
        // Given
        val duration = Duration.ofMinutes(30)

        // When
        val result = mapper.transform(duration)

        // Then
        assert(result == "30 minutes")
    }

    @Test
    fun `given a duration of 1 hour when transform then return a duration string`() {
        // Given
        val duration = Duration.ofHours(1)

        // When
        val result = mapper.transform(duration)

        // Then
        assert(result == "1 hour")
    }

    @Test
    fun `given a duration of 1 hour and 30 minutes when transform then return a duration string with hours and minutes`() {
        // Given
        val duration = Duration.ofHours(1).plusMinutes(30)

        // When
        val result = mapper.transform(duration)

        // Then
        assert(result == "1 hour 30 minutes")
    }

    @Test
    fun `given a duration of 2 hours when transform then return a duration string with hours and minutes`() {
        // Given
        val duration = Duration.ofHours(2)

        // When
        val result = mapper.transform(duration)

        // Then
        assert(result == "2 hours")
    }

    @Test
    fun `given a duration of 2 hours and 30 minutes when transform then return a duration string with hours and minutes`() {
        // Given
        val duration = Duration.ofHours(2).plusMinutes(30)

        // When
        val result = mapper.transform(duration)

        // Then
        assert(result == "2 hours 30 minutes")
    }
}
