package com.swenggco.contactapp.fragments


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.swenggco.contactapp.R
import com.swenggco.contactapp.base.BaseFragment
import com.swenggco.contactapp.injections.ViewModelFactory
import com.swenggco.contactapp.models.Contact
import com.swenggco.contactapp.viewmodels.ContactViewModel
import kotlinx.android.synthetic.main.fragment_contact_detail.*
import javax.inject.Inject


class ContactDetailFragment : BaseFragment(), DatePickerFragment.OnDateSelectedListener {


    private var listener: OnContactChangeListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

      var viewModel: ContactViewModel? = null

    companion object {
        const val TAG = "ContactDetailFragment"
         const val ARG_CONTACT = "ContactDetailFragment.contact"

        fun newInstance(contact: Contact): Fragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_CONTACT, contact)
            val fragment = ContactDetailFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(): Fragment {
            return ContactDetailFragment()
        }

    }

    override fun inject() {
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact_detail, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactViewModel::class.java)
        viewModel?.contact = arguments?.getParcelable(ARG_CONTACT) as? Contact
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnContactChangeListener) {
            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.inflateMenu(R.menu.menu_save)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_menu_save -> {

                    viewModel?.addContact(
                        organizationTextInput.getText(),
                        titleTextInput.getText(),
                        lastNameTextInput.getText(),
                        firstNameTextInput.getText(),
                        "+923316603001",
                        emailTextInput.getText(),
                        dateOfBirthTextView.text.toString(),
                        noteTextInput.getText(),
                        streetAddressTextInput.getText(),
                        zipTextInput.getText(),
                        cityTextInput.getText(),
                        countryTextInput.getText()
                    )
                }
            }
            return@setOnMenuItemClickListener true
        }


        viewModel?.insertResult?.observe(this, insertResultObserver)

        viewModel?.updateResult?.observe(this, updateResultObserver)

        viewModel?.contactObserver?.observe(this, Observer { contact ->
            contact?.let {
                loadContact(it)
            }

        })

        viewModel?.validation?.observe(this, validationObserver)

        viewModel?.errorMessage?.observe(this, Observer {
            toast(it)
        })

        val datePicker = DatePickerFragment()
        datePicker.listener = this

        dateOfBirthTextView.setOnClickListener {
            datePicker.show(childFragmentManager, "datePicker")
        }
    }

    override fun onDateSelected(year: Int, month: Int, day: Int) {
        dateOfBirthTextView.text = "$day-$month-$year"
    }

    private fun loadContact(contact: Contact) {


        contact.organization?.let {
            organizationTextInput.setText(it)
        }


        contact.title?.let {
            titleTextInput.setText(it)
        }

        lastNameTextInput.setText(contact.lastName)

        contact.firstName?.let {
            firstNameTextInput.setText(it)
        }

        contact.email?.let {
            emailTextInput.setText(it)
        }


        contact.dateBirth?.let {
            dateOfBirthTextView.text = it
        }

        contact.note?.let {
            noteTextInput.setText(it)
        }

        contact.address?.street?.let {
            streetAddressTextInput.setText(it)
        }

        contact.address?.zipCode?.let {
            zipTextInput.setText(it.toString())
        }

        contact.address?.city?.let {
            cityTextInput.setText(it)
        }

        contact.address?.country?.let {
            countryTextInput.setText(it)
        }

    }


    private val validationObserver: Observer<ContactViewModel.Validation> = Observer {

        lastNameTextInput.isErrorEnabled = false
        emailTextInput.isErrorEnabled = false
        when (it.ordinal) {
            ContactViewModel.Validation.LAST_NAME.ordinal -> {
                lastNameTextInput.isErrorEnabled = true
                lastNameTextInput.error = getString(it.msgId)
            }

            ContactViewModel.Validation.RESET.ordinal -> {
                lastNameTextInput.isErrorEnabled = false
            }

            ContactViewModel.Validation.EMAIL.ordinal -> {
                emailTextInput.isErrorEnabled = true
                emailTextInput.error = getString(it.msgId)
            }
        }
    }

    private val insertResultObserver: Observer<Long> = Observer {
        if (it > 0) {
            toast(getString(R.string.insert_successfully))
            listener?.onContactSaved()
        }
    }

    private val updateResultObserver: Observer<Int> = Observer {
        if (it > 0) {
            toast("Update contact successfully")
            listener?.onContactSaved()
        }
    }

    interface OnContactChangeListener {
        fun onContactSaved()
    }


}
