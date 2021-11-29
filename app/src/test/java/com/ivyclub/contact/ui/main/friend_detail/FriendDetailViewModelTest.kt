package com.ivyclub.contact.ui.main.friend_detail

import com.ivyclub.contact.InstantTaskExecutorRule
import com.ivyclub.contact.MainCoroutineRule
import com.ivyclub.contact.fake.FakeContactRepository
import com.ivyclub.data.model.FriendData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FriendDetailViewModelTest {
    private lateinit var viewModel: FriendDetailViewModel
    private lateinit var fakeRepository: FakeContactRepository

    @get:Rule
    var instanceExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeRepository = FakeContactRepository()
        viewModel = FriendDetailViewModel(fakeRepository)
        insertFriendsInFakeRepository()
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
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

    @ExperimentalCoroutinesApi
    @Test
    fun loadFriendIdCorrectly() {
        // when : id가 1인 유저를 받았을때
        runTest {
            viewModel.loadFriendData(1)
        }
        // then : id가 1인 유저의 이름이 mike1 이기때문에 확인
        assertEquals("mike1", viewModel.friendData.value?.name)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadFriendByIdIncorrectly() {
        // when : id가 1인 유저를 받았을때
        runTest {
            viewModel.loadFriendData(1)
        }
        // then : id가 1이 아닌 유저의 이름을 반환하면 오류
        assertNotEquals("mike5", viewModel.friendData.value?.name)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun setFavorite() {
        // when : id 가 1인 유저를 즐겨찾기에 등록
        runTest {
            viewModel.setFavorite(1, true)
            viewModel.loadFriendData(1)
        }
        // then : id 가 1인 유저의 isFavorite 값이 변경됨
        assertEquals(true, viewModel.friendData.value?.isFavorite)

        // when : id가 1인 유저를 즐겨찾기에서 해제했을 때
        runTest {
            viewModel.setFavorite(1, false)
            viewModel.loadFriendData(1)
        }
        // then : id 가 1인 유저의 isFavorite 값이 다시 false 로 변경됨
        assertEquals(false, viewModel.friendData.value?.isFavorite)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun removeFriendCorrectly() {
        // when : id 가 1인 친구가 삭제 되었다
        var friendList = listOf<FriendData>()
        runTest {
            viewModel.deleteFriend(1)
            friendList = fakeRepository.loadFriends()
        }
        // then : id 가 1인 친구는 삭제되었고 1이 아닌 친구는 삭제되지 않는다
        val deletedFriend: FriendData? = friendList.find { it.id == 1L }
        assertTrue(deletedFriend == null)

        val notDeletedFriend: FriendData? = friendList.find { it.id == 2L }
        assertTrue(notDeletedFriend != null)
    }
}