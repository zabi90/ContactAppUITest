package com.swenggco.contactapp.database


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.swenggco.contactapp.models.Address
import com.swenggco.contactapp.models.Contact
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class DataBaseHelperTest {


    lateinit var dataBaseHelper: DataBaseHelper


    @Before
    fun createDataBase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBaseHelper = DataBaseHelper(
            Room.inMemoryDatabaseBuilder(
                context, ContactDataBase::class.java
            )
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build()
        )
    }

    @Test
    fun insertContact() {
        assertNotNull(dataBaseHelper)
        dataBaseHelper.addContact(getDummyContact()).test().assertValue {
            return@assertValue it == 1L
        }

    }

    @Test
    fun getContact() {

        assertNotNull(dataBaseHelper)

        dataBaseHelper.addContact(getDummyContact()).test().assertValue {
            return@assertValue it == 1L
        }

        dataBaseHelper.getContacts().test().assertValue {
            return@assertValue it.size == 1
        }

        dataBaseHelper.getContacts().test().assertValue {
            if (it.isNotEmpty()) {
                return@assertValue it[0].firstName.equals("Zohaib")
            } else {
                return@assertValue false
            }

        }

    }

    @Test
    fun deleteContact() {
        assertNotNull(dataBaseHelper)

        var contact: Contact? = null

        dataBaseHelper.addContact(getDummyContact()).test().assertValue {
            return@assertValue it == 1L
        }

        dataBaseHelper.getContacts().test().assertValue {

            if (it.isNotEmpty()) {
                contact = it[0]
            }

            return@assertValue it.isNotEmpty()
        }



        dataBaseHelper.deleteContact(1).test().assertValue {
            return@assertValue it == 1
        }


    }

    private fun getDummyContact(): Contact {
        return Contact(
            "Zohaib",
            "Akram",
            "Office Contact",
            "+9203316603001",
            "zabi.gcu@gmail.com",
            Address("abc", "Pakistan", "Sialkot", "5123"),
            "This is my person detail",
            "Swenggco Software",
            "19-2-1990"
        )
    }

    private fun getBlankContact(): Contact {
        return Contact(
            null,
            "Akram",
            "Office Contact",
            "+9203316603001",
            "zabi.gcu@gmail.com",
            Address("abc", "Pakistan", "Sialkot", "5123"),
            "This is my person detail",
            "Swenggco Software",
            "19-2-1990"
        )
    }
}