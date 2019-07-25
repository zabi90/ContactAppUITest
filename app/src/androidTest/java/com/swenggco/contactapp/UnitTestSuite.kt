package com.swenggco.contactapp

import com.swenggco.contactapp.fragments.ContactDetailFragmentTest
import com.swenggco.contactapp.fragments.ContactListFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

// Runs all unit tests.
@RunWith(Suite::class)
@Suite.SuiteClasses(
    ContactListFragmentTest::class,
    ContactDetailFragmentTest::class)
class UnitTestSuite