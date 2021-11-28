package com.ivyclub.contact.friendviewmodel

import com.ivyclub.contact.fake.FakeContactRepository
import com.ivyclub.contact.ui.main.friend.FriendViewModel
import com.ivyclub.data.model.FriendData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FriendViewModelTest {

    private lateinit var viewModel: FriendViewModel
    private lateinit var fakeRepository: FakeContactRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeRepository = FakeContactRepository()
        viewModel = FriendViewModel(fakeRepository)
        insertFriendsInFakeRepository()
    }

    @Test
    fun invokeClearLongClickedId_returnLongClickedIdEmpty() {
        // when : clearLongClickedId()가 호출되었을 때
        viewModel.clearLongClickedId()

        // then : longClickedId 리스트가 클리어 된다
        assertTrue(viewModel.longClickedId.isEmpty())
    }

    @Test
    fun invokeSetSearchViewVisibility_returnChangedVisibility() {
        // given : 현재 searchViewVisible을 originSearchViewVisibility 저장했을 때
        val originSearchViewVisibility = viewModel.isSearchViewVisible.value

        // when : setSearchViewVisibility()가 호출되었을 때
        viewModel.setSearchViewVisibility()

        // then : isSearchViewVisibility가 변경되었는지 확인
        assertTrue(viewModel.isSearchViewVisible.value != originSearchViewVisibility)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun userSearchAtEditText_returnChangedFriendList() {
        // given : 먼저 친구 데이터를 데이터베이스로부터 수신하고,
        // 원래 친구 리스트를 originFriendList에 저장해둔다
        runTest {
            viewModel.getFriendDataWithFlow()
        }
        val originFriendList = viewModel.friendList.value

        // when : mike1이라고 검색했을 때(fake repository에는 mike0부터 mike10까지 들어있다)
        viewModel.onEditTextClicked("mike1")

        // then : changedFriendList에는 mike1과 mike10이 나온다.
        // filter된 리스트는 기존의 리스트와 달라야 한다.
        val filteredFriendList = viewModel.friendList.value
        assertTrue(originFriendList != filteredFriendList)
    }

    private fun insertFriendsInFakeRepository() {
        val friendsToInsert = mutableListOf<FriendData>()
        (0..10).forEach { i ->
            friendsToInsert.add(
                FriendData(
                    phoneNumber = "010" + i.toString().repeat(8),
                    name = "mike$i",
                    birthday = "1999.1.$i",
                    groupId = 1,
                    planList = emptyList(),
                    isFavorite = false,
                    extraInfo = emptyMap()
                )
            )
        }
        friendsToInsert.shuffle()
        runBlocking {
            friendsToInsert.forEach {
                fakeRepository.saveFriend(it)
            }
        }
    }
}