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

//    @ExperimentalCoroutinesApi
//    @Test
//    fun loadFriendIdCorrectly() {
//        // when : id??? 1??? ????????? ????????????
//        runTest {
//            viewModel.loadFriendData(1)
//        }
//        // then : id??? 1??? ????????? ????????? mike1 ??????????????? ??????
//        assert("mike1" == viewModel.friendData.value?.name)
//    }
//
//    @ExperimentalCoroutinesApi
//    @Test
//    fun loadFriendByIdIncorrectly() {
//        // when : id??? 1??? ????????? ????????????
//        runTest {
//            viewModel.loadFriendData(1)
//        }
//        // then : id??? 1??? ?????? ????????? ????????? ???????????? ??????
//        assert("mike5" != viewModel.friendData.value?.name)
//    }
//
//    @ExperimentalCoroutinesApi
//    @Test
//    fun setFavorite() {
//        // when : id ??? 1??? ????????? ??????????????? ??????
//        runTest {
//            viewModel.setFavorite(1, true)
//            viewModel.loadFriendData(1)
//        }
//        // then : id ??? 1??? ????????? isFavorite ?????? ?????????
//        assert(true == viewModel.friendData.value?.isFavorite)
//
//        // when : id??? 1??? ????????? ?????????????????? ???????????? ???
//        runTest {
//            viewModel.setFavorite(1, false)
//            viewModel.loadFriendData(1)
//        }
//        // then : id ??? 1??? ????????? isFavorite ?????? ?????? false ??? ?????????
//        assert(false == viewModel.friendData.value?.isFavorite)
//    }
//
//    @ExperimentalCoroutinesApi
//    @Test
//    fun removeFriendCorrectly() {
//        // when : id ??? 1??? ????????? ?????? ?????????
//        var friendList = listOf<FriendData>()
//        runTest {
//            viewModel.deleteFriend(1)
//            friendList = fakeRepository.loadFriends()
//        }
//        // then : id ??? 1??? ????????? ??????????????? 1??? ?????? ????????? ???????????? ?????????
//        val deletedFriend: FriendData? = friendList.find { it.id == 1L }
//        assert(deletedFriend == null)
//
//        val notDeletedFriend: FriendData? = friendList.find { it.id == 2L }
//        assert(notDeletedFriend != null)
//    }
}