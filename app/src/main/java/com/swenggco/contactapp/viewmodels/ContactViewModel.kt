package com.swenggco.contactapp.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.swenggco.contactapp.R
import com.swenggco.contactapp.base.BaseViewModel
import com.swenggco.contactapp.database.DataBaseHelper
import com.swenggco.contactapp.models.Address
import com.swenggco.contactapp.models.Contact
import com.swenggco.contactapp.util.EspressoIdlingResource
import javax.inject.Inject

class ContactViewModel @Inject constructor(private val dataBaseHelper: DataBaseHelper) : BaseViewModel() {

    var contact: Contact? = null
        set(value) {
            field = value
            contactObserver.value = value
        }

    val contactList by lazy {
        MutableLiveData<List<Contact>>()
    }


    val insertResult by lazy {
        MutableLiveData<Long>()
    }

    val updateResult by lazy {
        MutableLiveData<Int>()
    }

    val deleteResult by lazy {
        MutableLiveData<Int>()
    }

    val validation by lazy {
        MutableLiveData<Validation>()
    }


    val errorMessage by lazy {
        MutableLiveData<String>()
    }

    val contactObserver by lazy {
        MutableLiveData<Contact?>()
    }


    fun addContact(
        organization: String?,
        title: String?,
        lastName: String,
        firstName: String?,
        telephone: String?,
        email: String?,
        date: String?,
        note: String?,
        streetAddress: String?,
        zip: String?,
        city: String?,
        country: String?
    ) {


        if (lastName.isNotBlank() or lastName.isNotEmpty()) {

            if (!isValidEmail(email)) {
                return
            }
            validation.postValue(Validation.RESET)

            if (contact != null) {
                EspressoIdlingResource.increment()
                compositeDisposable.add(

                    dataBaseHelper.updateContact(
                        mapContact(
                            organization,
                            title,
                            lastName,
                            firstName,
                            telephone,
                            email,
                            date,
                            note,
                            streetAddress,
                            zip,
                            city,
                            country
                        )
                    ).compose(applySchedulers())
                        .subscribe({
                            EspressoIdlingResource.decrement()
                            updateResult.postValue(it)
                        }, {
                            Log.v("ContactViewModel", onHandleError(it))
                            EspressoIdlingResource.decrement()
                            errorMessage.postValue("Error while updating contact")
                        })

                )
            } else {
                EspressoIdlingResource.increment()
                compositeDisposable.add(
                    dataBaseHelper.addContact(
                        composeContact(
                            organization,
                            title,
                            lastName,
                            firstName,
                            telephone,
                            email,
                            date,
                            note,
                            streetAddress,
                            zip,
                            city,
                            country
                        )
                    )
                        .compose(applySchedulers())
                        .subscribe({
                            EspressoIdlingResource.decrement()
                            insertResult.postValue(it)
                        }, {
                            Log.v("ContactViewModel", onHandleError(it))
                            EspressoIdlingResource.decrement()
                            errorMessage.postValue("Error while inserting contact")
                        })
                )
            }


        } else {
            validation.postValue(Validation.LAST_NAME)
        }

    }

    fun deleteContact(id: Int) {
        EspressoIdlingResource.increment()
        compositeDisposable.add(
            dataBaseHelper.deleteContact(id)
                .compose(applySchedulers())
                .subscribe({
                    if (it == 1) {
                        EspressoIdlingResource.decrement()
                        deleteResult.postValue(it)
                    } else {
                        EspressoIdlingResource.decrement()
                        deleteResult.postValue(it)
                        errorMessage.postValue("Id not found")
                    }

                }, {
                    Log.v("ContactViewModel", onHandleError(it))
                    EspressoIdlingResource.decrement()
                    errorMessage.postValue("Error while deleteing contact")
                })
        )
    }

    fun getContacts() {
        EspressoIdlingResource.increment()
        compositeDisposable.add(
            dataBaseHelper.getContacts()
                .compose(applySchedulers())
                .subscribe({
                    EspressoIdlingResource.decrement()
                    contactList.postValue(it)
                }, {
                    Log.v("ContactViewModel", onHandleError(it))
                    EspressoIdlingResource.decrement()
                    errorMessage.postValue("Error while fetching contact list")
                })
        )
    }

    private fun mapContact(
        organization: String?,
        title: String?,
        lastName: String,
        firstName: String?,
        telephone: String?,
        email: String?,
        date: String?,
        note: String?,
        streetAddress: String?,
        zip: String?,
        city: String?,
        country: String?
    ): Contact {
        contact?.organization = organization
        contact?.title = title
        contact?.lastName = lastName
        contact?.firstName = firstName
        contact?.telephone = telephone
        contact?.email = email
        contact?.dateBirth = date
        contact?.note = note
        contact?.address?.city = city
        contact?.address?.country = country
        contact?.address?.zipCode = zip
        contact?.address?.street = streetAddress
        return contact!!
    }

    private fun composeContact(
        organization: String?,
        title: String?,
        lastName: String,
        firstName: String?,
        telephone: String?,
        email: String?,
        date: String?,
        note: String?,
        streetAddress: String?,
        zip: String?,
        city: String?,
        country: String?
    ): Contact {
        return Contact(
            firstName,
            lastName,
            title,
            telephone,
            email,
            Address(streetAddress, country, city, zip),
            note,
            organization,
            date
        )
    }

    private fun isValidEmail(email: String?): Boolean {

        return when {
            email == null -> true
            Patterns.EMAIL_ADDRESS.matcher(email).matches() -> true
            else -> {
                validation.postValue(Validation.EMAIL)
                false
            }
        }
    }

    enum class Validation(val msgId: Int) {
        LAST_NAME(R.string.validation_last_name),
        EMAIL(R.string.validation_email),
        RESET(0)
    }
}