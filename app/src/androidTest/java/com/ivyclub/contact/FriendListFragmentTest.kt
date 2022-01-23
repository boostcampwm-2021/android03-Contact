package com.ivyclub.contact

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ivyclub.contact.ui.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FriendListFragmentTest {
    private lateinit var scenario: ActivityScenario<MainActivity>
    private val friend1Name = "john"

    @Before
    fun setup() {
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun launchSettingsFragment() {
        onView(withId(R.id.iv_settings_icon)).perform(click())
        onView(withId(R.id.tv_get_contacts)).check(matches(withText(R.string.fragment_settings_get_contacts)))
    }

    // contact와 gildong이라는 이름을 넣어 친구가 추가되는지 확인
    @Test
    fun addFriend() {
        addFriendLogic(friend1Name)
    }

    // gildong이라는 친구 추가하고,
    // 검색했을 때 gildong이라는 친구가 나오는지 확인
    @Test
    fun addFriendAndSearchFriend() {
        addFriendLogic(friend1Name)
        onView(withId(R.id.iv_search)).perform(click())
        onView(withId(R.id.et_search)).perform(typeText(friend1Name))
        onView(withId(R.id.rv_friend_list)).check(matches(hasDescendant(withText(friend1Name))))
    }

    // 친구 리스트 중 첫 친구 클릭하는 이벤트
    @Test
    fun clickFirstFriendInRecyclerView() {
        onView(withId(R.id.rv_friend_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click())
        )
    }

    private fun addFriendLogic(friendName: String) {
        onView(withId(R.id.iv_add_friend_icon)).perform(click()) // plus 버튼 클릭
        onView(withText(R.string.menu_add_friend)).perform(click()) // 친구 추가하기 버튼 클릭
        onView(withId(R.id.et_name)).perform(typeText(friendName))
        closeSoftKeyboard()
        onView(withId(R.id.iv_save_icon)).perform(click()) // 체크 버튼 클릭
        onView(withText(friendName)).check(matches(isDisplayed()))
    }
}