package com.swenggco.contactapp.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swenggco.contactapp.RxImmediateSchedulerRule
import com.swenggco.contactapp.database.ContactDataBase
import com.swenggco.contactapp.database.DataBaseHelper
import com.swenggco.contactapp.util.OneTimeObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ContactViewModelTest {


    lateinit var dataBaseHelper: DataBaseHelper


    lateinit var viewModel: ContactViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Test rule for making the RxJava to run synchronously in unit test
    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }


    @Before
    fun setup() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        dataBaseHelper = DataBaseHelper(
            Room.inMemoryDatabaseBuilder(
                context, ContactDataBase::class.java
            )
                // allowing main thread queries, just for testing
                //.allowMainThreadQueries()
                .build()
        )

        viewModel = ContactViewModel(dataBaseHelper)
    }

    @Test
    fun addContactTest() {

        //given
        assertNotNull(viewModel)

        //when


        viewModel.addContact(
            "Swenngco", "Office Contact", "Akram", "Zohaib"
            , "+3311231", "zohaib@swenggco-software.com", "19-2-1990", "cool",
            "abc street", "12312", "sialkot", "Pakistan"
        )

        //then

        assertEquals(ContactViewModel.Validation.RESET, viewModel.validation.value)

        assertEquals(1L, viewModel.insertResult.value)


    }

    @Test
    fun addContactWithEmptyLastName() {
        //Given
        assertNotNull(viewModel)
        //When
        viewModel.addContact(
            "Swenngco", "Office Contact", "", "Zohaib"
            , "+3311231", "zohaib@swenggco-software.com", "19-2-1990", "cool",
            "abc street", "12312", "sialkot", "Pakistan"
        )

        //then
        assertEquals(ContactViewModel.Validation.LAST_NAME, viewModel.validation.value)
    }

    @Test
    fun getContact() {

        //Given
        assertNotNull(viewModel)
        //When

        viewModel.addContact(
            "Swenngco1", "Office Contact", "Akram", "Zohaib"
            , "+3311231", "zohaib@swenggco-software.com", "19-2-1990", "cool",
            "abc street", "12312", "sialkot", "Pakistan"
        )

        viewModel.addContact(
            "Swenngco2", "Office Contact", "Akram", "Zohaib"
            , "+3311231", "zohaib@swenggco-software.com", "19-2-1990", "cool",
            "abc street", "12312", "sialkot", "Pakistan"
        )

        viewModel.getContacts()


        assertEquals(2, viewModel.contactList.value!!.size)


    }

    @Test
    fun deleteContact() {
        //Given
        assertNotNull(viewModel)

        viewModel.addContact(
            "Swenngco2", "Office Contact", "Akram", "Zohaib"
            , "+3311231", "zohaib@swenggco-software.com", "19-2-1990", "cool",
            "abc street", "12312", "sialkot", "Pakistan"
        )
        //When
        viewModel.deleteContact(1)
        //Then
        assertEquals(1, viewModel.deleteResult.value)
    }

    @Test
    fun deleteWrongContact() {
        //Given
        assertNotNull(viewModel)

        viewModel.addContact(
            "Swenngco2", "Office Contact", "Akram", "Zohaib"
            , "+3311231", "zohaib@swenggco-software.com", "19-2-1990", "cool",
            "abc street", "12312", "sialkot", "Pakistan"
        )
        //When
        viewModel.deleteContact(2)
        //Then
        assertEquals(0, viewModel.deleteResult.value)
        assertEquals("Id not found", viewModel.errorMessage.value)

    }

    fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }
}