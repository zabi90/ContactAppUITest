package com.swenggco.contactapp.fragments

import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.textfield.TextInputLayout
import com.swenggco.contactapp.AndroidAppTest
import com.swenggco.contactapp.R
import com.swenggco.contactapp.injections.components.DaggerTestAppComponent
import com.swenggco.contactapp.injections.modules.AppModule
import com.swenggco.contactapp.util.EspressoIdlingResource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class ContactDetailFragmentTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var scenario: FragmentScenario<ContactDetailFragment>

    val contactDetailFragment = ContactDetailFragment()

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>() as AndroidAppTest
        context.component = DaggerTestAppComponent.builder()
            .appModule(AppModule(context))
            .build()

        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())
        //load fragment
        scenario = FragmentScenario.launchInContainer(
            ContactDetailFragment::class.java,
            null,
            R.style.AppTheme,
            object : FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                    return contactDetailFragment
                }
            })

    }

    @Test
    fun testLastNameEmpty() {

        //find toolbar
        onView(withId(R.id.toolbar))
            .perform(click())

        // Click on the save - we can find it by the r.Id.
        onView(withId(R.id.item_menu_save))
            .perform(click())

        //
        onView(withId(R.id.lastNameTextInput))
            .check(matches(errorTextInputLayout(R.string.validation_last_name)))

    }

    @Test
    fun invalidEmail() {

        onView(withId(R.id.lastNameTextInput))
            .perform(setTextInputLayout("Zohaib Akram"))


        onView(withId(R.id.emailTextInput))
            .perform(setTextInputLayout("wrong_email@com"))


        //find toolbar
        onView(withId(R.id.toolbar))
            .perform(click())

        // Click on the save - we can find it by the r.Id.
        onView(withId(R.id.item_menu_save))
            .perform(click())


        onView(withId(R.id.emailTextInput))
            .check(matches(errorTextInputLayout(R.string.validation_email)))

    }


    @Test
    fun saveContact() {

        val context = ApplicationProvider.getApplicationContext<Context>()


        onView(withId(R.id.organizationTextInput))
            .perform(setTextInputLayout("Swenggco Software"))

        onView(withId(R.id.titleTextInput))
            .perform(setTextInputLayout("Personal Number"))


        onView(withId(R.id.firstNameTextInput))
            .perform(setTextInputLayout("Zohaib"))

        onView(withId(R.id.lastNameTextInput))
            .perform(setTextInputLayout("Akram"))


        onView(withId(R.id.emailTextInput))
            .perform(setTextInputLayout("zabi.gcu@gmail.com"))


        onView(withId(R.id.streetAddressTextInput))
            .perform(setTextInputLayout("Abc Street"))


        onView(withId(R.id.zipTextInput))
            .perform(setTextInputLayout("12345"))

        onView(withId(R.id.cityTextInput))
            .perform(setTextInputLayout("Sialkot"))


        onView(withId(R.id.countryTextInput))
            .perform(setTextInputLayout("Pakistan"))

        onView(withId(R.id.dateOfBirthTextView)).perform(scrollTo(), click())


        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(1990, 3, 19))

        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.dateOfBirthTextView)).check(matches(withText("19-2-1990")))


        onView(withId(R.id.noteTextInput))
            .perform(setTextInputLayout("This is my personal contact"))


        //find toolbar
        onView(withId(R.id.toolbar))
            .perform(click())

        // Click on the save - we can find it by the r.Id.
        onView(withId(R.id.item_menu_save))
            .perform(click())



        onView(withText(context.getString(R.string.insert_successfully)))
            .inRoot(withDecorView(not(`is`(contactDetailFragment.activity?.window?.decorView))))
            .check(matches(isDisplayed()))

    }

    private fun errorTextInputLayout(errorId: Int): Matcher<View> {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val errorMessage = context.getString(errorId)

        return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("errorTextInputLayout : expected error message -> $errorMessage")
            }

            override fun matchesSafely(textInputLayout: TextInputLayout?): Boolean {
                return errorMessage == textInputLayout?.error
            }

        }
    }

    private fun setTextInputLayout(text: CharSequence): ViewAction {

        return object : ViewAction {

            override fun getDescription(): String {
                return "set text into input TextInputLayout"
            }

            override fun getConstraints(): Matcher<View> {

                return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
                    override fun describeTo(description: Description?) {
                    }

                    override fun matchesSafely(textInputLayout: TextInputLayout?): Boolean {
                        return true
                    }
                }
            }

            override fun perform(uiController: UiController?, view: View?) {
                val textInputLayout = view as? TextInputLayout
                textInputLayout?.editText?.setText(text, TextView.BufferType.EDITABLE)
            }
        }
    }
}