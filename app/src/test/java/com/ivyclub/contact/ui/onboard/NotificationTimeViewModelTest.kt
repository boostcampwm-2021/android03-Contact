package com.ivyclub.contact.ui.onboard

import com.ivyclub.contact.fake.FakeContactRepository
import com.ivyclub.contact.ui.onboard.notification.NotificationTimeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class NotificationTimeViewModelTest {
    private lateinit var viewModel: NotificationTimeViewModel
    private lateinit var fakeRepository: FakeContactRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
        fakeRepository = FakeContactRepository()
        viewModel = NotificationTimeViewModel(fakeRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun setNotificationTime_CheckNotificationTimeChanged() {
        // when notification 시간을 7시부터 23시로 설정했을 때
        runTest {
            viewModel.setTime(listOf(7F, 23F))
        }

        // then 저장된 notification 시간이 각각 7시와 23시가 맞는지 확인
        runTest {
            val notificationStartHour = fakeRepository.getStartAlarmHour()
            val notificationEndHour = fakeRepository.getEndAlarmHour()

            assert(notificationStartHour == 7)
            assert(notificationEndHour == 23)
        }
    }
}