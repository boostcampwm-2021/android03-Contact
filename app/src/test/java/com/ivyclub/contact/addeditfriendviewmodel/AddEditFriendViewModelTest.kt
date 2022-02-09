package com.ivyclub.contact.addeditfriendviewmodel

import com.ivyclub.contact.InstantTaskExecutorRule
import com.ivyclub.contact.R
import com.ivyclub.contact.fake.FakeContactRepository
import com.ivyclub.contact.ui.main.add_edit_friend.AddEditFriendViewModel
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.GroupData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddEditFriendViewModelTest {

    private lateinit var viewModel: AddEditFriendViewModel
    private lateinit var repository: FakeContactRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = FakeContactRepository()
        runTest {
            viewModel = AddEditFriendViewModel(repository)
        }
        initFakeFriendList()
        initFakeGroupList()
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getFriendData_parameterIsMinus1_friendDataIsNull() {
        // when
        viewModel.getFriendData(-1)

        // then
        assert(viewModel.friendData.value == null)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getFriendData_parameterIsNotMinus1_friendDataIsNotEmpty() {
        // when
        runTest {
            viewModel.getFriendData(1)
        }
        val expectedFriendId = 1L

        // then
        assert(viewModel.friendData.value != null)
        assertEquals(expectedFriendId, viewModel.friendData.value!!.id)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addExtraInfo_editFriend_extraInfoCountIncrease() {
        // given
        runTest {
            viewModel.getFriendData(2)
        }
        viewModel.addExtraInfoList(viewModel.friendData.value!!.extraInfo)
        val beforeExtraInfoCount = viewModel.friendData.value!!.extraInfo.size

        // when
        viewModel.addExtraInfo()

        // then
        assertEquals(beforeExtraInfoCount + 1, viewModel.extraInfos.value!!.size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addExtraInfo_addFriend_extraInfoCountIncrease() {
        // given
        val beforeExtraInfoCount = 0

        // when
        viewModel.addExtraInfo()

        // then
        assertEquals(beforeExtraInfoCount + 1, viewModel.extraInfos.value!!.size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addExtraInfoList_EqualWithFriendExtraInfoCount() {
        // given
        runTest {
            viewModel.getFriendData(2)
        }

        //when
        viewModel.addExtraInfoList(viewModel.friendData.value!!.extraInfo)

        // then
        val expected = viewModel.friendData.value!!.extraInfo.size
        assertEquals(expected, viewModel.extraInfos.value!!.size)
    }

    @Test
    fun removeExtraInfo_extraInfoCountDecrease() {
        // given
        repeat(5) {
            viewModel.addExtraInfo()
        }
        val beforeExtraInfoCount = viewModel.extraInfos.value!!.size

        // when
        viewModel.removeExtraInfo(1)

        // then
        assertEquals(beforeExtraInfoCount - 1, viewModel.extraInfos.value!!.size)
    }

    @Test
    fun checkNameValid_emptyString_returnFalse() {
        // when
        viewModel.checkNameValid("")

        // then
        assertEquals(R.string.add_edit_friend_required_check, viewModel.nameValidation.value!!)
        assertFalse(viewModel.isNameValid.value!!)
    }

    @Test
    fun checkNameValid_lengthOver15_returnFalse() {
        // when
        viewModel.checkNameValid("abcdeabcdeabcdeabcde")

        // then
        assertEquals(R.string.add_edit_friend_over_length, viewModel.nameValidation.value!!)
        assertFalse(viewModel.isNameValid.value!!)
    }

    @Test
    fun checkNameValid_validName_returnTrue() {
        // when
        viewModel.checkNameValid("홍길동")

        // then
        assertEquals(R.string.empty_string, viewModel.nameValidation.value!!)
        assertTrue(viewModel.isNameValid.value!!)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun saveFriendData_addFriend_friendListCountIncrease() {
        // when
        runTest {
            viewModel.saveFriendData("010-3333-3333",
                "홍길동",
                "2000.11.11",
                1,
                emptyList(),
                -1L
            )
        }

        // then
        lateinit var savedData: FriendData
        runTest {
            savedData = repository.getFriendDataById(3)
        }
        assertEquals("홍길동", savedData.name)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun saveFriendData_editFriend() {
        lateinit var beforeFriendData: FriendData
        lateinit var afterFriendData: FriendData

        runTest {
            // given
            beforeFriendData = repository.getFriendDataById(2)

            // when
            viewModel.saveFriendData("010-3333-3333",
                "김철수",
                "2000.10.10",
                1,
                emptyList(),
                2L
            )
        }

        runTest {
            afterFriendData = repository.loadFriends().find { it.id == 2L }!!
        }

        // then
        assertEquals(beforeFriendData.id, afterFriendData.id)
        assertNotEquals(beforeFriendData.phoneNumber, afterFriendData.phoneNumber)
        assertNotEquals(beforeFriendData.extraInfo, afterFriendData.extraInfo)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun createNewId_return3() {
        // when
        runTest {
            viewModel.createNewId()
        }

        // then
        assertEquals(3, viewModel.newId.value!!)
    }

    private fun initFakeFriendList() = runBlocking {
        repository.saveFriend(
            FriendData(
                phoneNumber = "010-1111-1111",
                name = "김영희",
                birthday = "2000.01.01",
                groupId = 1,
                planList = emptyList(),
                isFavorite = false,
                extraInfo = emptyMap(),
                id = 1
            )
        )
        repository.saveFriend(
            FriendData(
                phoneNumber = "010-2222-2222",
                name = "김철수",
                birthday = "2000.01.01",
                groupId = 1,
                planList = emptyList(),
                isFavorite = false,
                extraInfo = mapOf("mbti" to "isfp", "성격" to "온순"),
                id = 2
            )
        )
    }

    private fun initFakeGroupList() = runBlocking {
        repository.saveNewGroup(GroupData("친구", 1))
        repository.saveNewGroup(GroupData("부스트캠프", 2))
    }
}