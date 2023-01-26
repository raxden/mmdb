package com.raxdenstudios.app.core.domain

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ChangeHomeModuleFilterUseCaseTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val homeModuleRepository: HomeModuleRepository = mockk {
        coEvery { fetch(any()) } returns ResultData.Success(module)
        coEvery { save(any()) } returns ResultData.Success(true)
    }
    private val useCase: ChangeHomeModuleFilterUseCase by lazy {
        ChangeHomeModuleFilterUseCase(
            homeModuleRepository = homeModuleRepository,
        )
    }

    @Test
    fun `Should change home module filter`() = runTest {
        val params = ChangeHomeModuleFilterUseCase.Params(
            moduleId = moduleId,
            mediaType = mediaType,
        )

        val result = useCase(params)

        assertThat(result).isEqualTo(ResultData.Success(true))
        coVerify(exactly = 1) { homeModuleRepository.save(module) }
    }

    companion object {

        private val module = HomeModule.Popular.empty
        private val moduleId = module.id
        private val mediaType = MediaType.Movie
    }
}
