package com.ivyclub.contact.friendviewmodel

import com.ivyclub.contact.fake.FakeContactRepository
import com.ivyclub.contact.ui.main.friend.FriendViewModel
import com.ivyclub.data.model.FriendData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun invokeClearLongClickedId_ReturnLongClickedIdEmpty() {
        // when : clearLongClickedId()가 호출되었을 때
        viewModel.clearLongClickedId()

        // then : longClickedId 리스트가 클리어 된다
        assertTrue(viewModel.longClickedId.isEmpty())
    }

    @Test
    fun invokeSetSearchViewVisibility_ReturnChangedVisibility() {
        // given : 현재 searchViewVisible을 originSearchViewVisibility 저장했을 때
        val originSearchViewVisibility = viewModel.isSearchViewVisible.value

        // when : setSearchViewVisibility()가 호출되었을 때
        viewModel.setSearchViewVisibility()

        // then : isSearchViewVisibility가 변경되었는지 확인
        assertTrue(viewModel.isSearchViewVisible.value != originSearchViewVisibility)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun invokeOnEditTextClicked_ReturnChangedFriendList() {
        // given : 먼저 친구 데이터를 데이터베이스로부터 수신하고,
        // 원래 친구 리스트를 originFriendList에 저장해둔다
        runTest {
            viewModel.getFriendDataWithFlow()
        }
        val originFriendList = viewModel.friendList.value
        val searchText = "mike1"

        // when : mike1이라고 검색했을 때(fake repository에는 mike0부터 mike10까지 들어있다)
        viewModel.onEditTextClicked(searchText)
        val filteredFriendList = viewModel.friendList.value

        // then : filteredFriendList에는 mike1과 mike10이 나온다.
        // filter된 리스트는 기존의 리스트와 달라야 한다.
        // 또한 filter된 리스트가 비어있지 않다면 리스트 중 하나 이상의 아이템에는 mike1 string이 포함되어야 한다.
        assertTrue(originFriendList != filteredFriendList)
        assert(if (filteredFriendList.isNotEmpty()) filteredFriendList.any {
            it.name.contains(
                searchText
            )
        } else true)
    }

    // 롱클릭했을 때 isInLongClickedState으로 들어가는지 확인한다
    @ExperimentalCoroutinesApi
    @Test
    fun invokeSetLongClickedId_ReturnChangedLongClickedState() {
        // given : 먼저 친구 데이터를 데이터베이스로부터 수신하고,
        runTest {
            viewModel.getFriendDataWithFlow()
        }

        // when : 1번 id를 롱클릭한다
        viewModel.setLongClickedId(true, 1)

        // then : 정상적으로 롱클릭 상태로 진입했는지 확인한다
        assertTrue(viewModel.isInLongClickedState.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun invoke_ReturnLoadSuccessfully() {
        // given : 친구 데이터를 수신
        runTest {
            viewModel.getFriendDataWithFlow()
        }

        // then : 친구리스트가 정상적으로 채워졌는지 확인한다
        assert(viewModel.friendList.value.isNotEmpty())
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
                    extraInfo = emptyMap(),
                    id = i.toLong()
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