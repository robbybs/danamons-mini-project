package com.rbs.danamontest.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rbs.danamontest.DataDummy
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.data.repository.HomeRepository
import getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserInteractorTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var homeRepository: HomeRepository
    private lateinit var homeInteractor: HomeInteractor
    private val userList = DataDummy.getData()

    @Before
    fun setUp() {
        homeInteractor = HomeInteractor(homeRepository)
    }

    @Test
    fun `when Get User List Should Not Null and Return Success`() {
        val observer = Observer<List<UserEntity>> {}
        try {
            val expectedValue = MutableLiveData<List<UserEntity>>()
            expectedValue.value = userList
            `when`(homeRepository.getAllUser()).thenReturn(expectedValue)
            val actualValue = homeInteractor.getAllUser().getOrAwaitValue()
            verify(homeRepository).getAllUser()
            assertNotNull(actualValue)
            assertEquals(userList.size, actualValue.size)
        } finally {
            homeInteractor.getAllUser().removeObserver(observer)
        }
    }
}