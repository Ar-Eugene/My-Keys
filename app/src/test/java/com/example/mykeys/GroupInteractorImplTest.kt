package com.example.mykeys

import com.example.mykeys.newGroup.domain.interactor.impl.GroupInteractorImpl
import com.example.mykeys.newGroup.domain.models.GroupModel
import com.example.mykeys.newGroup.domain.repository.GroupRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
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
    fun `getGroup - should return flow from repository`() = runTest {
        // создаем список для теста
        val testFlow = flowOf(listOf(mockk<GroupModel>(), mockk<GroupModel>()))
        // создаем мок
        every { repository.getGroup() } returns testFlow

        val resultTest = interactor.getGroup()

        // проверяем, что Flow содержит ожидаемый результат
        assertThat(resultTest).isEqualTo(testFlow)

        // проверяем, что реп вызывается
        verify(exactly = 1) { repository.getGroup() }
    }

    @Test
    fun `createGroup - check that the repository is created by the group`() = runTest {
        // grate test object
        val group = mockk<GroupModel>()
        /** mockk is the suspend create through coEvery
         * tell that any argument GroupModel correspond this rule
         * "just Runs" tell that the method should simply return without returning a value
         * createGroup this suspend method and return type Unit
         */
        coEvery { repository.createGroup(any()) } just Runs

        interactor.createGroup(group)

        //coVerify in suspend method analogue verify
        coVerify(exactly = 1) { repository.createGroup(group) }
    }

    @Test
    fun `deleteGroupById - check that the repository is delete group by ID`() = runTest {
        // create test data
        val testId = 1

        // create mockk for suspend method
        coEvery { repository.deleteGroupById(any()) } just Runs

        // call test method
        interactor.deleteGroupById(testId)

        coVerify(exactly = 1) { repository.deleteGroupById(testId) }
    }

    @Test
    fun `updateGroup - check that the repository update group`() = runTest {
        val group = mockk<GroupModel>()

        coEvery { repository.updateGroup(any()) } just Runs

        interactor.updateGroup(group)

        coVerify(exactly = 1) { repository.updateGroup(group) }
    }

    @Test
    fun `isGroupNameExists - check that the group exists with nameGroup`() = runTest {

        val testName = "Test"
        val testId = 1

        coEvery { repository.isGroupNameExists(testName, testId) } returns true

        val result = interactor.isGroupNameExists(testName, testId)

        assertThat(result).isTrue()

        coVerify(exactly = 1) { repository.isGroupNameExists(testName, testId) }
    }

    @Test
    fun `isGroupNameExists - check that the group does not exist with nameGroup`() = runTest {
        // Подготавливаем тестовые данные
        val testName = "Test"
        val testId = 1

        // Настраиваем мок для suspend метода
        coEvery { repository.isGroupNameExists(testName, testId) } returns false

        // Вызываем тестируемый метод
        val result = interactor.isGroupNameExists(testName, testId)

        // Проверяем результат
        assertThat(result).isFalse()

        // Проверяем, что метод репозитория был вызван с правильными параметрами
        coVerify(exactly = 1) { repository.isGroupNameExists(testName, testId) }
    }
}