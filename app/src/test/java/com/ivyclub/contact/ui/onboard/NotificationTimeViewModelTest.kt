package com.ivyclub.contact.ui.onboard

import com.ivyclub.contact.ui.onboard.notification.NotificationTimeViewModel
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.ContactPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NotificationTimeViewModelTest {
    @Mock
    private lateinit var viewModel: NotificationTimeViewModel

    @Mock
    private lateinit var contactPreference: ContactPreference

    @Mock
    private lateinit var repository: ContactRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(StandardTestDispatcher())
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
        val startHourByUser = 7F
        val endHourByUser = 23F
        runTest {
            viewModel.setTime(listOf(startHourByUser, endHourByUser))
            `when`(repository.getStartAlarmHour()).thenReturn(startHourByUser.toInt())
            `when`(repository.getEndAlarmHour()).thenReturn(endHourByUser.toInt())
        }

        // then 저장된 notification 시간이 각각 7시와 23시가 맞는지 확인
        runTest {
            val notificationStartHour = repository.getStartAlarmHour()
            val notificationEndHour = repository.getEndAlarmHour()

            assert(notificationStartHour == 7)
            assert(notificationEndHour == 23)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun setNotificationOff_CheckNotificationOff() {
        val notificationState = false
        runTest {
            `when`(viewModel.setNotificationOnOff(notificationState)).then {
                `when`(contactPreference.getNotificationState()).thenReturn(notificationState)
            }
            viewModel.setNotificationOnOff(notificationState)
        }

        assert(contactPreference.getNotificationState() == notificationState)
    }
}