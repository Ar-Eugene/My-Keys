package com.example.mykeys

import com.example.mykeys.newGroup.domain.interactor.impl.GroupInteractorImpl
import com.example.mykeys.newGroup.domain.models.GroupModel
import com.example.mykeys.newGroup.domain.repository.GroupRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GroupInteractorImplTest {
    private lateinit var interactor: GroupInteractorImpl
    private val repository: GroupRepository = mockk()

    @BeforeEach
    fun setUp() {
        interactor = GroupInteractorImpl(repository)
    }

    @Test
    fun `getGroup should return flow from repository`() = runTest {
        // создаем список для теста
        val testFlow = listOf(
            GroupModel(
                id = 1,
                nameGroup = "nameGroup",
                emailGroup = "emailGroup",
                passwordGroup = "passwordGroup",
                loginGroup = "loginGroup",
                position = 1,
            ), GroupModel(
                id = 2,
                nameGroup = "nameGroup2",
                emailGroup = "emailGroup2",
                passwordGroup = "passwordGroup2",
                loginGroup = "loginGroup2",
                position = 2,
            )
        )
        // создаем мок
        every { repository.getGroup() } returns flowOf(testFlow)

        val resultTest = interactor.getGroup()
        // проверяем, что Flow содержит ожидаемый результат
        resultTest.collect { group ->
            assertThat(group).isEqualTo(testFlow)
        }
        // проверяем, что реп вызывается
        verify { repository.getGroup() }
    }
}