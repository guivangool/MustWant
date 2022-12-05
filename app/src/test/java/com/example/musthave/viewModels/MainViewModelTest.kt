package com.example.musthave.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.Repositories.MainRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    @RelaxedMockK
    private lateinit var mainRepository:MainRepository

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        mainViewModel = MainViewModel(mainRepository)

    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun Test1 () = runTest {
        //Given
        coEvery { mainRepository.getSelectedGoals() } returns ArrayList<GoalEntity>(0)
        coEvery { mainRepository.getGoals() } returns ArrayList<GoalEntity>(0)
        //When
        mainRepository.getSelectedGoals()
        mainRepository.getGoals()
        //Then

        coVerify(exactly = 1) { mainRepository.insertGoal(GoalEntity(1, false, 0)) }
        coVerify(exactly = 1) { mainRepository.insertGoal(GoalEntity(2, false, 0)) }
        coVerify(exactly = 1) { mainRepository.insertGoal(GoalEntity(3, false, 0)) }
        coVerify(exactly = 1) { mainRepository.insertGoal(GoalEntity(4, false, 0)) }
        assert(mainViewModel.goalsSelection.value!!.size == 0)
    }
}