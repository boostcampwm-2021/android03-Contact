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
        MockitoAnnotations.initMocks(this)
        viewModel = FriendViewModel(repository)
    }

    @Test
    fun testClearLongClickedIdVariable() {
        viewModel.clearLongClickedId()
        assertTrue(viewModel.longClickedId.isEmpty())
    }
}