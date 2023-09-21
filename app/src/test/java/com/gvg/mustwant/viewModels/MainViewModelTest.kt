package com.gvg.mustwant.viewModels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gvg.mustwant.DataEntities.GoalEntity
import com.gvg.mustwant.DomainEntities.MainMessage
import com.gvg.mustwant.Enums.MainMessageEnum
import com.gvg.mustwant.Repositories.MainRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

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
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun  `when there are not goals selected and  in the database the app has to insert all of them with default values` () = runTest {
        //Given
        coEvery { mainRepository.getSelectedGoals() } returns ArrayList<GoalEntity>(0)
        coEvery { mainRepository.getGoals() } returns ArrayList<GoalEntity>(0)

        //When
        mainViewModel = MainViewModel(mainRepository)

        //Then
        coVerify(exactly = 1) { mainRepository.insertGoal(GoalEntity(1, false, 0,0,0)) }
        coVerify(exactly = 1) { mainRepository.insertGoal(GoalEntity(2, false, 0,0,0)) }
        coVerify(exactly = 1) { mainRepository.insertGoal(GoalEntity(3, false, 0,0,0)) }
        coVerify(exactly = 1) { mainRepository.insertGoal(GoalEntity(4, false, 0,0,0)) }

        assert(mainViewModel.goalsSelection.value!!.size == 0)

    }

    @Test
    fun `when there are goals in the database the app does not insert default values`() = runTest {
        //Given
        var selectedGoalsTest = ArrayList<GoalEntity>()
        selectedGoalsTest.add(GoalEntity(1,true,0,0,0))
        coEvery { mainRepository.getSelectedGoals() } returns selectedGoalsTest
        coEvery { mainRepository.getGoals() } returns selectedGoalsTest

        //When
        mainViewModel = MainViewModel(mainRepository)

        //Then
        coVerify(exactly = 0) { mainRepository.insertGoal(GoalEntity(1, false, 0,0,0)) }
        coVerify(exactly = 0) { mainRepository.insertGoal(GoalEntity(2, false, 0,0,0)) }
        coVerify(exactly = 0) { mainRepository.insertGoal(GoalEntity(3, false, 0,0,0)) }
        coVerify(exactly = 0) { mainRepository.insertGoal(GoalEntity(4, false, 0,0,0))}
        assert(mainViewModel.goalsSelection.value!!.size == 1)
    }

    @Test
    fun `when there are not goals selected, a message informing that is shown`() = runTest {
        //Given
        var selectedGoalsTest = ArrayList<GoalEntity>()
        var message = MainMessage(MainMessageEnum.NO_SELECTED_GOALS.number,MainMessageEnum.NO_SELECTED_GOALS.message,"")
        coEvery { mainRepository.getSelectedGoals() } returns ArrayList<GoalEntity>(0)

        //When
        mainViewModel = MainViewModel(mainRepository)
        val result = runBlocking {
            async {
                mainViewModel.setMainMessage(selectedGoalsTest)
            }
        }

        //Then
        assert( mainViewModel.mainMessage.value?.messageNumber == message.messageNumber)
    }

    @Test
    fun `when there are goals selected but no progress, a message informing that is shown`() = runTest {
        //Given
        var selectedGoalsTest = ArrayList<GoalEntity>()
        selectedGoalsTest.add(GoalEntity(1,true,0,0,0))
        var message = MainMessage(MainMessageEnum.NO_GOAL_PROGRESS.number,MainMessageEnum.NO_GOAL_PROGRESS.message,"")
        coEvery { mainRepository.getSelectedGoals() } returns selectedGoalsTest
        coEvery { mainRepository.getGoals() } returns selectedGoalsTest
        coEvery { mainRepository.getAllFromYesterday(Date())} returns 0

        //When
        mainViewModel = MainViewModel(mainRepository)
        val result = runBlocking {
            async {
                mainViewModel.setMainMessage(selectedGoalsTest)
            }
        }

        //Then
        assert( mainViewModel.mainMessage.value?.messageNumber == message.messageNumber)
    }
}