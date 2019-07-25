package com.swenggco.contactapp.database

import com.swenggco.contactapp.models.Contact
import io.reactivex.Single

open class DataBaseHelper constructor(private val dataBase: ContactDataBase) {


    companion object {
        const val DATA_BASE_NAME = "Contact_APP_DB"
    }

    fun addContact(contact: Contact): Single<Long> {
        return dataBase.contactDao().insertContact(contact)
    }

    fun getContacts(): Single<List<Contact>> {
        return dataBase.contactDao().getAll()
    }

    fun deleteContact(id: Int): Single<Int> {
        return dataBase.contactDao().deleteContact(id)
    }

    fun updateContact(contact: Contact): Single<Int> {
        return dataBase.contactDao().updateContact(contact)
    }
}