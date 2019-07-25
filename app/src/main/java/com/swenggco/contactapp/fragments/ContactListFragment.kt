package com.swenggco.contactapp.fragments


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.swenggco.contactapp.R
import com.swenggco.contactapp.adapter.ContactListAdapter
import com.swenggco.contactapp.base.BaseFragment
import com.swenggco.contactapp.base.OnItemSelectListener
import com.swenggco.contactapp.injections.ViewModelFactory
import com.swenggco.contactapp.models.Contact
import com.swenggco.contactapp.viewmodels.ContactViewModel
import kotlinx.android.synthetic.main.fragment_contact_list.*
import javax.inject.Inject


class ContactListFragment : BaseFragment() {


    companion object {
        const val TAG = "ContactListFragment"
    }

    var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapater: ContactListAdapter

    override fun inject() {
        component.inject(this)
    }

     var viewModel: ContactViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.inflateMenu(R.menu.menu_add)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.item_menu_add -> {
                    listener?.addNewContact()
                }
            }
            return@setOnMenuItemClickListener true
        }

        loadContacts()
    }



    fun loadContacts() {

        context?.let {
            if (recyclerView == null) {
                return
            }
            adapater = ContactListAdapter(it)
            recyclerView.layoutManager = LinearLayoutManager(it)
            recyclerView.hasFixedSize()
            recyclerView.adapter = adapater

            viewModel?.getContacts()

            viewModel?.contactList?.observe(this, Observer { contactList ->
                if(contactList.isEmpty()){
                    emptyTextView.visibility = View.VISIBLE
                }else{
                    emptyTextView.visibility = View.GONE
                }
                adapater.setItems(contactList)
            })

            adapater.addListener(object : OnItemSelectListener<Contact> {
                override fun onItemSelected(item: Contact, position: Int, view: View) {
                    //launch fragment
                    listener?.loadContactDetail(contact = item)
                }
            })
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }


    interface OnFragmentInteractionListener {
        fun addNewContact()
        fun loadContactDetail(contact: Contact)
    }

}
