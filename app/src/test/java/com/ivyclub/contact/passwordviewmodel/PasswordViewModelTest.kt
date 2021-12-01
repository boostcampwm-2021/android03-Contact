package com.ivyclub.contact.passwordviewmodel

import com.ivyclub.contact.InstantTaskExecutorRule
import com.ivyclub.contact.fake.FakeContactRepository
import com.ivyclub.contact.ui.password.PasswordViewModel
import com.ivyclub.contact.util.PasswordViewType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class PasswordViewModelTest {

    private lateinit var viewModel: PasswordViewModel
    private lateinit var repository: FakeContactRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mock(FakeContactRepository::class.java)
        viewModel = PasswordViewModel(repository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun initPasswordViewType_APP_CONFIRM() {
        // when
        runTest {
            `when`(repository.getPassword()).thenReturn("\$2a\$10\$wM/3Ld9e.1Dvcl29ztfHo.PFdK2BCYRj07VJbsgbWlR7A7QQgfoiS")
            viewModel.initPasswordViewType(PasswordViewType.APP_CONFIRM_PASSWORD)
        }

        // then
        assert(viewModel.password.isNotEmpty())
        assertEquals("\$2a\$10\$wM/3Ld9e.1Dvcl29ztfHo.PFdK2BCYRj07VJbsgbWlR7A7QQgfoiS", viewModel.password)
    }

    @Test
    fun moveFocusBack_focusedEditTextIndexIs1_focusedEditTextNotChange() {
        // when
        viewModel.moveFocusBack()

        // then
        assertEquals(1, viewModel.focusedEditTextIndex.value)
    }

    @Test
    fun moveFocusBack_focusedEditTextIndexIs3_focusedEditTextIs2_updatePassword2() {
        // given
        viewModel.moveFocusFront("0")
        viewModel.moveFocusFront("0")

        // when
        viewModel.moveFocusBack()

        // then
        assertEquals(2, viewModel.focusedEditTextIndex.value)
        assertEquals("", viewModel.password2.value)
    }

    @Test
    fun moveFocusFront_focusedEditTextIndexIs1_focusedEditTextIndexIs2_updatePassword1() {
        // when
        viewModel.moveFocusFront("0")

        // then
        assertEquals(2, viewModel.focusedEditTextIndex.value)
        assertEquals("0", viewModel.password1.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun moveFocusFront_focusedEditTextIndexIs4_incorrectPassword() {
        // given
        runTest {
            `when`(repository.getPassword()).thenReturn("\$2a\$10\$wM/3Ld9e.1Dvcl29ztfHo.PFdK2BCYRj07VJbsgbWlR7A7QQgfoiS")
            viewModel.initPasswordViewType(PasswordViewType.APP_CONFIRM_PASSWORD)
        }
        viewModel.moveFocusFront("0")
        viewModel.moveFocusFront("0")
        viewModel.moveFocusFront("0")

        // when
        viewModel.moveFocusFront("0")

        // then
        assertEquals(1, viewModel.focusedEditTextIndex.value)
        assertEquals("", viewModel.password1.value)
        assertEquals("", viewModel.password2.value)
        assertEquals("", viewModel.password3.value)
        assertEquals("", viewModel.password4.value)
    }

}