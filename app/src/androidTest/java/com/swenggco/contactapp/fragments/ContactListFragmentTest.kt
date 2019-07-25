package com.swenggco.contactapp.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.swenggco.contactapp.AndroidAppTest
import com.swenggco.contactapp.R
import com.swenggco.contactapp.adapter.ContactListAdapter
import com.swenggco.contactapp.injections.components.DaggerTestAppComponent
import com.swenggco.contactapp.injections.modules.AppModule
import com.swenggco.contactapp.util.EspressoIdlingResource
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ContactListFragmentTest {


    lateinit var scenario: FragmentScenario<ContactListFragment>

    val contactFragment = ContactListFragment()

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>() as AndroidAppTest
        context.component = DaggerTestAppComponent.builder()
            .appModule(AppModule(context))
            .build()

        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())
        //load fragment
        scenario = FragmentScenario.launchInContainer(
            ContactListFragment::class.java,
            null,
            R.style.AppTheme,
            object : FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                    return contactFragment
                }
            })

    }

    @Test
    fun checkEmptyContactList() {
        //when
        scenario.recreate()
        val emptyTextView = onView(withId(R.id.emptyTextView))

        emptyTextView
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.you_don_t_have_contact)))
    }

    @Test
    fun checkListWithContacts() {


        contactFragment.viewModel!!.addContact(
            "Swenngco", "Office Contact", "Akram", "Zohaib"
            , "+3311231", "zohaib@swenggco-software.com", "19-2-1990", "cool",
            "abc street", "12312", "sialkot", "Pakistan"
        )


        scenario.recreate()

        val recycleListView = onView(withId(R.id.recyclerView))

        recycleListView.check(matches(isDisplayed()))

        onView(withId(R.id.emptyTextView)).check(matches(not(isDisplayed())))

        //check recycler view with data
        recycleListView.perform(
            RecyclerViewActions.actionOnItemAtPosition<ContactListAdapter.ContactViewHolder>(
                0,
                click()
            )
        )
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource())
    }

}