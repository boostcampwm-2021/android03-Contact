package com.ivyclub.contact.planviewmodel

import android.text.format.DateUtils
import com.ivyclub.contact.InstantTaskExecutorRule
import com.ivyclub.contact.ui.main.plan.PlanViewModel
import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.SimpleFriendData
import com.ivyclub.data.model.SimplePlanData
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.sql.Date

@ExperimentalCoroutinesApi
class PlanViewModelTest {

    private lateinit var viewModel: PlanViewModel
    private lateinit var repository: ContactRepository

    private val planList = mutableListOf<SimplePlanData>()
    private val friendList = mutableListOf<SimpleFriendData>()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mock(ContactRepository::class.java)
        viewModel = PlanViewModel(repository)

        initMockRepository()
        initPlanViewModel()
    }

    private fun initMockRepository() {
        var time = System.currentTimeMillis()
        repeat(10) {
            time += (DateUtils.DAY_IN_MILLIS * 10)
            val date = Date(time)
            planList.add(
                SimplePlanData(
                    it.toLong(), "plan $it", date, listOf(it.toLong())
                )
            )
        }

        `when`(repository.loadPlanListWithFlow()).thenReturn(
            flow { emit(planList) }
        )

        repeat(10) {
            friendList.add(
                SimpleFriendData(
                    it.toLong(), "friend $it", "010-1234-000$it"
                )
            )
        }

        runBlocking {
            `when`(repository.getSimpleFriendData()).thenReturn(friendList)
        }
    }

    private fun initPlanViewModel() {
        val method = viewModel.javaClass.getDeclaredMethod("getMyPlans")
        method.isAccessible = true
        runTest { method.invoke(viewModel) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun invokeOnCreated_ReturnPlanListItemsNotNullOrEmpty() {
        // when : 약속 리스트 화면 진입
        // then : 전체 약속 리스트를 liveData에 설정
        assertNotNull(viewModel.planListItems.value)
        viewModel.planListItems.value?.let {
            assertTrue(it.isNotEmpty())
        }
    }

    @Test
    fun invokeFriendNameChangedAndRefreshPlanList_ReturnPlanListItemsChanged() {
        // given : id가 0인 지인 이름 변경
        val newSimpleFriendData = SimpleFriendData(0L, "person", "010-1234-0000")
        friendList.removeFirst()
        friendList.add(0, newSimpleFriendData)

        val beforePlanListItems = viewModel.planListItems.value
        // when : 약속 리스트 갱신
        runTest { viewModel.refreshPlanItems() }

        // then : 이전 약속 리스트와 비교 -> 달라짐
        val afterPlanListItems = viewModel.planListItems.value
        assertTrue(beforePlanListItems != afterPlanListItems)
    }


}