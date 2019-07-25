package com.swenggco.contactapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.swenggco.contactapp.fragments.ContactDetailFragment
import com.swenggco.contactapp.fragments.ContactListFragment
import com.swenggco.contactapp.models.Contact

class MainActivity : AppCompatActivity(), ContactListFragment.OnFragmentInteractionListener,
    ContactDetailFragment.OnContactChangeListener {


    override fun loadContactDetail(contact: Contact) {

        val contactDetailFragment = ContactDetailFragment.newInstance(contact) as ContactDetailFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, contactDetailFragment)
            .addToBackStack(ContactDetailFragment.TAG).commit()
    }


    override fun onContactSaved() {

        val contactListFragment: ContactListFragment? =
            supportFragmentManager.findFragmentByTag(ContactListFragment.TAG)
                    as ContactListFragment
        contactListFragment?.loadContacts()
    }

    override fun addNewContact() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ContactDetailFragment.newInstance())
            .addToBackStack(ContactDetailFragment.TAG).commit()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
