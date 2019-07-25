package com.swenggco.contactapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swenggco.contactapp.R
import com.swenggco.contactapp.base.BaseRecyclerAdapter
import com.swenggco.contactapp.models.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactListAdapter(context: Context) :
    BaseRecyclerAdapter<Contact, ContactListAdapter.ContactViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(parent.inflate(R.layout.item_contact))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = items[position]
        holder.bindItem(contact)
    }


    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(contact: Contact) {
            itemView.nameTextView.text = contact.firstName ?: "".plus(contact.lastName)
            itemView.emailTextView.text = contact.email ?: ""
            itemView.setOnClickListener {
                listener?.onItemSelected(contact, adapterPosition, itemView)
            }
        }
    }
}