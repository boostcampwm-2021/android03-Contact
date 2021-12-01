package com.ivyclub.contact.ui.onboard

import com.ivyclub.contact.fake.FakeContactRepository
import com.ivyclub.data.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class OnBoardingViewModelTest {
    @Mock
    private lateinit var repository: ContactRepository // 추후 Mockito로 대체해보기
    private lateinit var viewModel: OnBoardingViewModel
    private lateinit var fakeRepository: FakeContactRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        fakeRepository = FakeContactRepository()
        viewModel = OnBoardingViewModel(fakeRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun launchApp_CheckFriendGroupInitialized() {
        runTest {
            viewModel.saveDefaultGroup()
        }
        runTest {
            val firstInputGroup = fakeRepository.loadGroupsWithFlow().first()[0]
            assert(firstInputGroup.name == "친구")
            assert(firstInputGroup.id == 1L)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun launchApp_SetOnBoardingStateFalse() {
        viewModel.setFirstOnBoardingStateFalse()

        runTest {
            assertFalse(fakeRepository.getShowOnBoardingState())
        }
    }
}