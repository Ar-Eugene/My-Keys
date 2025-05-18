package com.example.mykeys.newGroup.data.impl

import com.example.mykeys.newGroup.data.db.converter.GroupDbConverter
import com.example.mykeys.newGroup.data.db.dao.GroupDao
import com.example.mykeys.newGroup.data.db.entity.GroupEntity
import com.example.mykeys.newGroup.domain.models.GroupModel
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GroupRepositoryImplTest {
    private lateinit var repository: GroupRepositoryImpl
    private val groupDao: GroupDao = mockk()
    private val groupDbConverter: GroupDbConverter = mockk()

    @BeforeEach
    fun setUp() {
        repository = GroupRepositoryImpl(groupDao, groupDbConverter)
    }

    /**
     * Тест проверяет
     * Правильность преобразования данных из DAO в модели
     * Корректность вызова методов DAO и конвертера
     * Правильность порядка вызовов (сначала DAO, потом конвертер)
     * Соответствие входных и выходных данных
     */
    @Test
    fun `getGroup - should return converted flow from dao`() = runTest {
        // Подготавливаем тестовые данные
        // список сущностей (List<GroupEntity>)
        val testEntitiesList = listOf(mockk<GroupEntity>(), mockk<GroupEntity>())
        //Flow, содержащий список выше
        val testEntitiesFlow = flowOf(testEntitiesList)
        val testModels = listOf(mockk<GroupModel>(), mockk<GroupModel>())

        /**
         * Настраиваем моки
         * сначала говорим, что что DAO должен вернуть Flow с нашими тестовыми сущностями
         * делаее указываем, что конвертер должен преобразовать сущности в модели
         */
        every { groupDao.getGroup() } returns testEntitiesFlow
        every { groupDbConverter.mapEntityListToModelList(testEntitiesList) } returns testModels

        // Вызываем тестируемый метод
        val result = repository.getGroup().first()

        // Проверяем результат
        assertThat(result).isEqualTo(testModels)

        // Проверяем вызовы методов
        verify(exactly = 1) { groupDao.getGroup() }
        verify(exactly = 1) { groupDbConverter.mapEntityListToModelList(testEntitiesList) }
    }

    /**
     * Тест проверяет:
     * 1. Правильность конвертации GroupModel в GroupEntity
     * 2. Корректность вызова метода insertGroup в DAO
     * 3. Правильность порядка вызовов (сначала конвертер, потом DAO)
     */
    @Test
    fun `createGroup - should create Group and save in Room`() = runTest {
        // Подготавливаем тестовые данные
        val testModel = mockk<GroupModel>()
        val testEntity = mockk<GroupEntity>()

        // Настраиваем моки
        every { groupDbConverter.mapModelToEntity(testModel) } returns testEntity
        coEvery { groupDao.insertGroup(testEntity) } just Runs

        // Вызываем тестируемый метод
        repository.createGroup(testModel)

        // Проверяем вызовы методов
        verify(exactly = 1) { groupDbConverter.mapModelToEntity(testModel) }
        coVerify(exactly = 1) { groupDao.insertGroup(testEntity) }
    }

    @Test
    fun `deleteGroupById - should delete group for ID from Room`() = runTest {
        val testId = 1

        coEvery { groupDao.deleteGroupById(testId) } just Runs

        repository.deleteGroupById(testId)

        coVerify(exactly = 1) { groupDao.deleteGroupById(testId) }
    }

    @Test
    fun `updateGroup - should delete group in Room`() = runTest {
        val testModel = mockk<GroupModel>()
        val testEntity = mockk<GroupEntity>()

        every { groupDbConverter.mapModelToEntity(testModel) } returns testEntity
        coEvery { groupDao.updateGroup(testEntity) } just Runs

        repository.updateGroup(testModel)

        verify(exactly = 1) { groupDbConverter.mapModelToEntity(testModel) }
        coVerify(exactly = 1) { groupDao.updateGroup(testEntity) }
    }

    /**
     * Тест проверяет:
     * Правильность преобразования результата из DAO в Boolean
     * Случай, когда группа не существует ( = 0)
     */
    @Test
    fun `isGroupNameExists - check that the group does not exist with nameGroup`() = runTest {
        // Подготавливаем тестовые данные
        val testName = "Test"
        val testId = 1

        coEvery { groupDao.isGroupNameExists(testName, testId) } returns 0

        val result = repository.isGroupNameExists(testName, testId)

        assertThat(result).isFalse()

        coVerify(exactly = 1) { groupDao.isGroupNameExists(testName, testId) }
    }

    /**
     * Тест проверяет:
     * Случай, когда группа существует ( > 0)
     */
    @Test
    fun `isGroupNameExists - check that the group exists with nameGroup`() = runTest {
        val testName = "Test"
        val testId = 1

        coEvery { groupDao.isGroupNameExists(testName, testId) } returns 1

        val result = repository.isGroupNameExists(testName, testId)

        assertThat(result).isTrue()

        coVerify(exactly = 1) { groupDao.isGroupNameExists(testName, testId) }
    }
}