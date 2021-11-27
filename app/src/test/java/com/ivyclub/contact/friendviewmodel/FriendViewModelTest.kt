package com.ivyclub.contact.friendviewmodel

import com.ivyclub.contact.MainCoroutineRule
import com.ivyclub.contact.ui.main.friend.FriendViewModel
import com.ivyclub.data.ContactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FriendViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: ContactRepository
    private lateinit var viewModel: FriendViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = FriendViewModel(repository)
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
        // given : 현재 searchViewVisible을 originSearchViewVisibility 저장
        val originSearchViewVisibility = viewModel.isSearchViewVisible.value

        // when : setSearchViewVisibility()가 호출되었을 때
        viewModel.setSearchViewVisibility()

        // then : isSearchViewVisibility가 변경되었는지 확인
        assertTrue(viewModel.isSearchViewVisible.value != originSearchViewVisibility)
    }
}